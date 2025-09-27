/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 62.5, "KoPercent": 37.5};
    var dataset = [
        {
            "label" : "FAIL",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "PASS",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.5820512820512821, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [1.0, 500, 1500, "Open Product Page"], "isController": false}, {"data": [0.0, 500, 1500, "Submit Order"], "isController": false}, {"data": [0.9923076923076923, 500, 1500, "Homepage"], "isController": true}, {"data": [1.0, 500, 1500, "CartPage"], "isController": false}, {"data": [0.0, 500, 1500, "Checkout"], "isController": true}, {"data": [0.0, 500, 1500, "Open Checkout Page"], "isController": false}, {"data": [0.0, 500, 1500, "Send Message"], "isController": false}, {"data": [1.0, 500, 1500, "View Cart"], "isController": false}, {"data": [1.0, 500, 1500, "Open Contact Page"], "isController": false}, {"data": [0.9923076923076923, 500, 1500, "Open Homepage"], "isController": false}, {"data": [1.0, 500, 1500, "Cart"], "isController": true}, {"data": [0.0, 500, 1500, "Contact"], "isController": true}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 2080, 780, 37.5, 27.1216346153846, 9, 639, 16.0, 40.0, 46.0, 73.19000000000005, 103.4980345325173, 597.4540082164004, 21.136744601184255], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["Open Product Page", 260, 0, 0.0, 14.873076923076926, 9, 48, 11.0, 37.0, 39.0, 43.389999999999986, 13.461737599668634, 87.29424130488246, 2.5766607124365746], "isController": false}, {"data": ["Submit Order", 260, 260, 100.0, 37.434615384615384, 33, 64, 37.0, 39.0, 40.0, 42.16999999999996, 13.489675210127634, 6.494540897063401, 3.3460717806371276], "isController": false}, {"data": ["Homepage", 260, 0, 0.0, 51.41923076923078, 9, 639, 41.5, 68.0, 89.94999999999999, 633.39, 12.952722562646342, 168.2535989731231, 2.276845762965177], "isController": true}, {"data": ["CartPage", 260, 0, 0.0, 12.8576923076923, 9, 43, 11.0, 15.0, 36.94999999999999, 41.389999999999986, 13.482679941920763, 120.06948360687618, 2.4885024502177973], "isController": false}, {"data": ["Checkout", 260, 260, 100.0, 75.15769230769232, 68, 102, 75.0, 78.0, 79.0, 85.38999999999999, 13.462434629524155, 13.002292820897841, 5.8766682415989235], "isController": true}, {"data": ["Open Checkout Page", 260, 260, 100.0, 37.723076923076924, 34, 50, 37.0, 40.0, 41.0, 46.16999999999996, 13.487575867614254, 6.533044560875655, 2.54209193598589], "isController": false}, {"data": ["Send Message", 260, 260, 100.0, 37.54999999999999, 34, 99, 37.0, 39.0, 40.0, 47.94999999999993, 13.493875856342122, 6.536096117915715, 3.729264518891426], "isController": false}, {"data": ["View Cart", 260, 0, 0.0, 13.126923076923074, 9, 43, 11.0, 14.900000000000006, 37.94999999999999, 41.389999999999986, 13.486176668914363, 111.68483194149074, 2.489147842211733], "isController": false}, {"data": ["Open Contact Page", 260, 0, 0.0, 11.988461538461538, 9, 43, 11.0, 13.0, 15.0, 38.389999999999986, 13.511406745309982, 109.02848600789899, 2.4938045652964718], "isController": false}, {"data": ["Open Homepage", 260, 0, 0.0, 51.41923076923078, 9, 639, 41.5, 68.0, 89.94999999999999, 633.39, 13.035848583604913, 169.3333914358235, 2.2914577588368013], "isController": false}, {"data": ["Cart", 260, 0, 0.0, 40.86153846153848, 28, 128, 34.0, 63.0, 90.94999999999999, 121.38999999999999, 13.446421183285064, 318.2971024740122, 7.537349374224245], "isController": true}, {"data": ["Contact", 260, 260, 100.0, 49.538461538461576, 44, 111, 48.0, 52.0, 56.0, 78.94999999999993, 13.485477178423237, 115.35127885114107, 6.21596213692946], "isController": true}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Median
            case 8:
            // Percentile 1
            case 9:
            // Percentile 2
            case 10:
            // Percentile 3
            case 11:
            // Throughput
            case 12:
            // Kbytes/s
            case 13:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["404/Not Found", 780, 100.0, 37.5], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 2080, 780, "404/Not Found", 780, "", "", "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": ["Submit Order", 260, 260, "404/Not Found", 260, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["Open Checkout Page", 260, 260, "404/Not Found", 260, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Send Message", 260, 260, "404/Not Found", 260, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});

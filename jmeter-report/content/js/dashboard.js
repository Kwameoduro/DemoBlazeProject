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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.5817307692307693, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [1.0, 500, 1500, "Open Product Page"], "isController": false}, {"data": [0.0, 500, 1500, "Submit Order"], "isController": false}, {"data": [0.9903846153846154, 500, 1500, "Homepage"], "isController": true}, {"data": [1.0, 500, 1500, "CartPage"], "isController": false}, {"data": [0.0, 500, 1500, "Checkout"], "isController": true}, {"data": [0.0, 500, 1500, "Open Checkout Page"], "isController": false}, {"data": [0.0, 500, 1500, "Send Message"], "isController": false}, {"data": [1.0, 500, 1500, "View Cart"], "isController": false}, {"data": [1.0, 500, 1500, "Open Contact Page"], "isController": false}, {"data": [0.9903846153846154, 500, 1500, "Open Homepage"], "isController": false}, {"data": [1.0, 500, 1500, "Cart"], "isController": true}, {"data": [0.0, 500, 1500, "Contact"], "isController": true}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 2080, 780, 37.5, 46.212980769230825, 22, 680, 24.0, 69.0, 78.0, 92.57000000000016, 101.152555560959, 368.3456215350387, 20.657742364927298], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["Open Product Page", 260, 0, 0.0, 26.661538461538477, 22, 72, 23.0, 25.0, 66.94999999999999, 71.0, 13.257865483657131, 71.94279318685942, 2.5376383152312476], "isController": false}, {"data": ["Submit Order", 260, 260, 100.0, 65.95384615384616, 61, 118, 65.0, 68.0, 69.0, 81.5799999999997, 13.285641287685232, 6.3965093254982115, 3.295461803781298], "isController": false}, {"data": ["Homepage", 260, 0, 0.0, 73.01923076923076, 22, 680, 77.0, 85.0, 120.0, 673.39, 12.779552715654951, 63.80771534775129, 2.246405750798722], "isController": true}, {"data": ["CartPage", 260, 0, 0.0, 25.31153846153845, 22, 72, 23.0, 24.0, 62.999999999999545, 70.38999999999999, 13.285641287685232, 76.49707979368932, 2.4521349642309658], "isController": false}, {"data": ["Checkout", 260, 260, 100.0, 131.72307692307692, 125, 183, 131.0, 135.0, 136.95, 158.50999999999988, 13.23963743762094, 12.787508593033913, 5.7794120455239835], "isController": true}, {"data": ["Open Checkout Page", 260, 260, 100.0, 65.76923076923076, 61, 92, 65.0, 68.0, 69.0, 73.11999999999989, 13.286999182338512, 6.43608985333197, 2.504287931827473], "isController": false}, {"data": ["Send Message", 260, 260, 100.0, 65.66153846153847, 61, 86, 65.0, 68.0, 69.0, 70.77999999999997, 13.288357354594705, 6.436548093631811, 3.672465948584279], "isController": false}, {"data": ["View Cart", 260, 0, 0.0, 23.576923076923084, 22, 68, 23.0, 24.0, 25.0, 46.86999999999955, 13.315579227696404, 76.64685267399877, 2.457660619174434], "isController": false}, {"data": ["Open Contact Page", 260, 0, 0.0, 23.749999999999996, 22, 69, 23.0, 24.0, 24.0, 66.38999999999999, 13.318307550455895, 76.5567072565567, 2.4581641865587542], "isController": false}, {"data": ["Open Homepage", 260, 0, 0.0, 73.01923076923076, 22, 680, 77.0, 85.0, 120.0, 673.39, 12.855377008652658, 64.18630253399257, 2.2597342398022247], "isController": false}, {"data": ["Cart", 260, 0, 0.0, 75.55000000000004, 66, 205, 69.0, 73.0, 116.89999999999998, 181.0399999999995, 13.226840311339473, 224.069004123849, 7.414264002645368], "isController": true}, {"data": ["Contact", 260, 260, 100.0, 89.41153846153843, 84, 137, 88.0, 91.0, 92.0, 131.39, 13.272077590607452, 82.71962935490046, 6.117598264420622], "isController": true}]}, function(index, item){
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

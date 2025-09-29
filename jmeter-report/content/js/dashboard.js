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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 2080, 780, 37.5, 25.363461538461518, 8, 591, 14.0, 38.0, 41.0, 69.0, 103.50833540681761, 667.8679106276437, 21.138848283155014], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["Open Product Page", 260, 0, 0.0, 12.53461538461539, 8, 41, 10.0, 15.800000000000011, 37.0, 38.389999999999986, 13.431139580535179, 107.12625271205702, 2.5708040603368114], "isController": false}, {"data": ["Submit Order", 260, 260, 100.0, 36.52307692307696, 32, 54, 36.0, 39.0, 39.0, 46.16999999999996, 13.423511797201714, 6.462686832051216, 3.329660152821519], "isController": false}, {"data": ["Homepage", 260, 0, 0.0, 46.3423076923077, 8, 591, 37.0, 63.900000000000006, 82.0, 587.17, 12.952722562646342, 158.1936867932048, 2.276845762965177], "isController": true}, {"data": ["CartPage", 260, 0, 0.0, 12.396153846153844, 8, 53, 10.0, 15.0, 36.0, 39.559999999999945, 13.432527381690432, 133.91554742392537, 2.4792457765034097], "isController": false}, {"data": ["Checkout", 260, 260, 100.0, 72.75384615384614, 67, 91, 72.0, 76.0, 77.0, 85.16999999999996, 13.397918169638256, 12.93998151344945, 5.8485052947542], "isController": true}, {"data": ["Open Checkout Page", 260, 260, 100.0, 36.23076923076922, 33, 51, 36.0, 38.0, 39.0, 40.389999999999986, 13.422818791946309, 6.501677852348993, 2.5298867449664426], "isController": false}, {"data": ["Send Message", 260, 260, 100.0, 36.33461538461537, 33, 42, 36.0, 38.0, 39.0, 40.389999999999986, 13.424898022409252, 6.502684979604481, 3.710201308927557], "isController": false}, {"data": ["View Cart", 260, 0, 0.0, 11.4, 8, 40, 10.0, 12.0, 17.0, 39.0, 13.435998139631026, 131.1410242849207, 2.4798863753811173], "isController": false}, {"data": ["Open Contact Page", 260, 0, 0.0, 11.146153846153846, 8, 40, 10.0, 12.0, 13.949999999999989, 39.0, 13.441555084526703, 137.7657918077599, 2.4809120224370576], "isController": false}, {"data": ["Open Homepage", 260, 0, 0.0, 46.3423076923077, 8, 591, 37.0, 63.900000000000006, 82.0, 587.17, 13.03780964797914, 159.23286907030388, 2.291802477183833], "isController": false}, {"data": ["Cart", 260, 0, 0.0, 36.33076923076926, 25, 87, 31.0, 57.0, 60.0, 81.77999999999997, 13.416584963104391, 371.71831863744256, 7.520624774240156], "isController": true}, {"data": ["Contact", 260, 260, 100.0, 47.48076923076925, 42, 77, 47.0, 49.0, 51.0, 75.77999999999997, 13.416584963104391, 144.00852525607615, 6.18420713143093], "isController": true}]}, function(index, item){
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

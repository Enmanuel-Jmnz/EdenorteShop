

function downloadCharts(chartID) {
               
                chart = PF(chartID).exportAsImage();
                
                var link = document.createElement('a');

                link.href = chart.src;
                link.download = chartID + '.png';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);

                
 }

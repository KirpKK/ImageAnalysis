'use strict';
// import '../views/css/app.css';
import $ from 'jquery';

function showImage() {
    window.location.replace("http://localhost:8080/image");
}
$('#openMenu').click(showImage());

$('#transpose').click(function ()  {
    alert("transpose!!!");
});

$('#cancel').click(function ()  {
    alert("cancel!!!");
});


// function createNewDiagram() {
//     openDiagram(String(newDiagramXML));
// }
//
// function openDiagram(xml) {
//
//     modeler.importXML(xml, function(err) {
//
//         if (err) {
//             container
//                 .removeClass('with-diagram')
//                 .addClass('with-error');
//
//             container.find('.error pre').text(err.message);
//
//             console.error(err);
//         } else {
//             container
//                 .removeClass('with-error')
//                 .addClass('with-diagram');
//         }
//
//
//     });
// }
//
// function saveSVG(done) {
//     modeler.saveSVG(done);
// }
//
// function saveDiagram(done) {
//
//     modeler.saveXML({ format: true }, function(err, xml) {
//         done(err, xml);
//     });
// }
//
// function registerFileDrop(container, callback) {
//
//     function handleFileSelect(e) {
//         e.stopPropagation();
//         e.preventDefault();
//
//         var files = e.dataTransfer.files;
//
//         var file = files[0];
//
//         var reader = new FileReader();
//
//         reader.onload = function(e) {
//
//             var xml = e.target.result;
//
//             callback(xml);
//         };
//
//         reader.readAsText(file);
//     }
//
//     function handleDragOver(e) {
//         e.stopPropagation();
//         e.preventDefault();
//
//         e.dataTransfer.dropEffect = 'copy'</p>\n<p>// Explicitly show this is a copy.
//     }
//
//     container.get(0).addEventListener('dragover', handleDragOver, false);
//     container.get(0).addEventListener('drop', handleFileSelect, false);
// }
//
// ///////////////////    DB   /////////////////
// function insertDiagramDB() {
//     var name = prompt("Enter diagram's name", 'diagram');
//
//     var nano = nanoImp('http://127.0.0.1:5984');
//     var diagrams = nano.db.use('diagrams');
//
//     var diagram;
//     modeler.saveXML({ format: true }, function(err, xml) {
//         diagram = xml;
//     });
//     var img;
//     modeler.saveSVG({ format: true }, function(err, svg) {
//         img = svg;
//     });
//
//     //Insert diagram
//     if (typeof(diagram) == 'undefined'){
//         alert('Diagram is undefined');
//     } else {
//         diagrams.insert({_id: name, diagram: String(diagram), img: String(img)}, name, function(err, body) {
//             if (!err){
//                 console.log(body);
//             } else {
//                 alert(err);
//             }
//         });
//     }
// }
//
// function openDiagramDB(name) {
//     var nano = nanoImp('http://127.0.0.1:5984');
//     var diagrams = nano.db.use('diagrams');
//
//     var b = false;
//     diagrams.view('view_diagram/', 'by_key', { include_docs: true
//     }, function(err, body){
//         body.rows.forEach(function(row) {
//             if(row.id==String(name)){
//                 b = true;
//                 if (typeof(row.doc.diagram)=='string') {
//                     openDiagram(row.doc.diagram);
//                 } else {
//                     alert('Diagram is undefined');
//                 }
//             }
//         });
//         if (!b) alert('No diagram is found');
//     });
// }
//
// function showPreview() {
//     name = prompt("Enter diagram's name", 'diagram');
//
//     var nano = nanoImp('http://127.0.0.1:5984');
//     var diagrams = nano.db.use('diagrams');
//
//     var b = false;
//     diagrams.view('view_diagram/', 'by_key', { include_docs: true
//     }, function(err, body){
//         body.rows.forEach(function(row) {
//             if(row.id==String(name)){
//                 b = true;
//                 if (typeof(row.doc.diagram)=='string') {
//                     var num = row.doc.img.indexOf('<svg');
//                     var svg = row.doc.img.substr(num);
//                     var newSvg = svg.substr(0,5) + 'style="max-width:550px;max-height:350px;" ' + svg.substr(5);
//                     $('#svg').append(newSvg);
//                     $('#confirm').append('<a class="prv">Open diagram ' + name + '?</a>');
//                 } else {
//                     alert('Diagram is undefined');
//                 }
//             }
//         });
//         if (!b) alert('No diagram is found');
//     });
// }
//
// function showAllDiagrams() {
//     preview.hide();
//     intro.hide();
//
//     var nano = nanoImp('http://127.0.0.1:5984');
//     var diagrams = nano.db.use('diagrams');
//
//     var b = false;
//     diagrams.view('view_diagram/', 'by_key', { include_docs: true
//     }, function(err, body){
//         body.rows.forEach(function(row) {
//             b = true;
//             $('#allDiagrams').append('<li>' + row.id + '</li>');
//         });
//         if (!b) alert('No diagram is found');
//     });
// }
//
// ////// file drag / drop ///////////////////////
//
// // check file api availability
// if (!window.FileList || !window.FileReader) {
//     window.alert(
//         'Looks like you use an older browser that does not support drag and drop. ' +
//         'Try using Chrome, Firefox or the Internet Explorer > 10.');
// } else {
//     registerFileDrop(container, openDiagram);
// }
//
// // bootstrap diagram functions
//
// $(function() {
//
//     function setEncoded(link, name, data) {
//         var encodedData = encodeURIComponent(data);
//
//         if (data) {
//             link.addClass('active').attr({
//                 'href': 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData,
//                 'download': name
//             });
//         } else {
//             link.removeClass('active');
//         }
//     }
//
//     $('#js-create-diagram').click(function(e) {
//         e.stopPropagation();
//         e.preventDefault();
//
//         createNewDiagram();
//     });
//
//     var downloadLink = $('#js-download-diagram');
//     var downloadSvgLink = $('#js-download-svg');
//
//     $('#js-download-db').click(function(e) {
//         e.stopPropagation();
//         e.preventDefault();
//         insertDiagramDB();
//     });
//
//     $('#js-open-db').click(function(e) {
//         e.stopPropagation();
//         e.preventDefault();
//         showAll.show();
//         showAllDiagrams();
//     });
//
//     $('#open-preview').click(function(e) {
//         e.stopPropagation();
//         e.preventDefault();
//         preview.show();
//         intro.hide();
//         showAll.hide();
//         showPreview();
//     });
//
//     $('#open-diagram').click(function(e) {
//         e.stopPropagation();
//         e.preventDefault();
//         preview.hide();
//         showAll.hide();
//         openDiagramDB(name);
//     });
//
//     $('#cancel').click(function(e) {
//         intro.show();
//         preview.hide();
//         showAll.hide();
//         $("svg").remove();
//         $("a.prv").remove();
//     });
//
//     $('.buttons a').click(function(e) {
//         if (!$(this).is('.active')) {
//             e.preventDefault();
//             e.stopPropagation();
//         }
//     });
//
//     var _ = require('lodash');
//
//     var exportArtifacts = _.debounce(function() {
//
//         saveSVG(function(err, svg) {
//             setEncoded(downloadSvgLink, 'diagram.svg', err ? null : svg);
//         });
//
//         saveDiagram(function(err, xml) {
//             setEncoded(downloadLink, 'diagram.bpmn', err ? null : xml);
//         });
//
//     }, 500);
//
//     modeler.on('commandStack.changed', exportArtifacts);
// });
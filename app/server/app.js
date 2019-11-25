const express = require('express');
const app = express();
var connect = require('connect');

app.use(express.static(__dirname + '/public'));
//app.use(connect.logger('dev'));
//app.use(connect.urlencoded());

require('./route/routes.js')(app);

app.listen(6000, () => {
    console.log("Map Memo App listening on port 6000");
})
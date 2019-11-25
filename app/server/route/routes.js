var mysql = require('mysql');
var dbconfig = require('../config/database.js');
var connection = mysql.createConnection(dbconfig);
var bodyParser = require('body-parser');

module.exports = function(app) {

    app.post('/addMemoImage', (req,res) => {
        var inputData;

        req.on('data', (data) => {
            inputData = JSON.parse(data);
            memo = {
                'android_id' : inputData.androidId,
                'image' : "aa",
                'location' : inputData.location
            };
        });

    
        req.on('end', () => {
            

            connection.query('insert into image SET ?', memo , function(error, result) {
                if(error){
                    console.log("error 발생 : ", error);
                    res.write("error");
                    res.end();
                }
                else {
                    console.log("데이터 삽입완료");
                    res.write("addMemoSuccess");
                    res.end();
                }
            });
        });
    })

}
//importing modules 
var express = require('express');
var mongoose = require('mongoose');
var cors = require('cors');
var bodyparser = require('body-parser');
var path = require('path');

//routing files
const route_structure = require('./routes/route_structure.js');
const route_data = require('./routes/route_data.js');
const route_user = require('./routes/route_user.js');

//test for trial

var app = express();
//port no
const port = 3000;

//adding middleware cors and bodyparser
app.use(cors());
app.use(bodyparser.json()); 
app.use(express.urlencoded());

//routes
app.use('/api/structure', route_structure);
app.use('/api/data', route_data);
app.use('/api/user', route_user);


server_uri = "mongodb+srv://sugamDBMaster:sugam@2021@cluster0.dfum6.mongodb.net/<dbname>?retryWrites=true&w=majority"
// server_uri = "mongodb+srv://logger_admin:Ntpc@2020@cluster0.t5byo.mongodb.net/<dbname>?retryWrites=true&w=majority"
const uri = server_uri
mongoose.connect(uri, { useNewUrlParser: true });

//on connection
mongoose.connection.on('connected', () => {
    console.log('Connected to database mongodb');
});

//on error 
mongoose.connection.on('error', (err) => {
    if(err){
        console.log('Error occured while connecting to mongodb' + err);
    }
});

//testing server 
app.get('/', (req, res) => {
    res.send('foobar');
})



//static files
app.use(express.static(path.join(__dirname, 'public')));


app.get('/', (req, res)=>{
    res.send("Server up and running"); 
})
.listen(process.env.PORT || 3000)
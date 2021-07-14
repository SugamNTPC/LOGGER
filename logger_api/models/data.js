const mongoose = require('mongoose');
const moment = require('moment');

const DataSchema = mongoose.Schema({
    id : {
        type : String,
        required : true
    },
    logger_id : {
        type : String,
        required : false
    },
    logger_name : {
        type : String,
        required : false
    },
    value : {
        type : String, 
        required : false
    },
    entry_time : {
        type : Date,
        // default : moment.utc().toDate(Date.now) ,
        // default : Date.now,
        required : true
    }
});

const Data = module.exports = mongoose.model('Data', DataSchema);
const mongoose = require('mongoose');

const UserSchema = mongoose.Schema({
    phone : {
        type : String,
        required : true
    },
    password : {
        type : String,
        required : true
    },
    name : {
        type : String, 
        required : false
    },
    root_node : {
        type : String,
        required : false
    },
    root_node_name : {
        type : String,
        required : false
    },
    webapp_access : {
        type : String,
        required : false
    },
    app_access : {
        type : String,
        required : false
    }
});

const User = module.exports = mongoose.model('User', UserSchema);
const mongoose = require('mongoose');

const StructureSchema = mongoose.Schema({
    id : {
        type : String,
        required : true
    },
    name : {
        type : String,
        required : true
    },
    parent : {
        type : String, 
        required : true
    },
    level_leaf : {
        type : String,
        required : false
    },
    dtype : {
        type : String,
        required : true
    },
    slider_entries : {
        type : String,
        required : false
    },
    low_lim : {
        type : String,
        required : false
    },
    high_lim : {
        type : String,
        required : false
    },
    disable_entry : {
        type : String, 
        required : false
    },
    hint_text : {
        type : String,
        required : false
    }, 
    default_value : {
        type : String, 
        required : false
    },
    unit : {
        type : String,
        required : false
    },
    value : {
        type : String,
        required : false
    }
});

const Structure = module.exports = mongoose.model('Structure', StructureSchema);
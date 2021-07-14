const express = require('express');
const extend = require('extend');
const router = express.Router();

const Data = require('../models/data');

// getting datas
router.get('/', (req, res, next)=>{
    Data.find((err, datas)=>{
        res.json(datas);
    })
});

// getting datas
router.post('/history', (req, res, next)=>{
    var id = req.body.id;
    // console.log(id)
    Data.find({id : id},
        ['_id','value','entry_time','logger_id','logger_name'], // Columns to Return
        {
            skip:0, // Starting Row
            limit: 100, // Ending Row
            sort:{
                _id: -1 //Sort by Date Added DESC
            }
        }
    ,(err, datas)=>{
        for(i=0;i<datas.length;i++){
            data = datas[i];
            data.entry_time = data._id.getTimestamp();
            // console.log(data._id.getTimestamp())
        }
        res.json(datas);
    })
});

//adding datas
router.post('/',(req, res, next)=>{
    let newData = new Data(req.body);
    newData.save((err, data)=>{
        if(err){
            //console.log(err);
            res.json("Error occured in saving : " + err);
        }
        else{
            res.json(data);
        }
    })
})

router.post('/update',(req, res, next)=>{
    data = req.body.array;
    let datanew = JSON.parse(data);
    // for(i=0;i<datanew.length; i++){
    //     let 
    // }
     Data.collection.insertMany(datanew, function (err, docs) {
        if (err){ 
            // return console.error(err);
            res.json("Error occured");
        } else {
        //   console.log("Multiple documents inserted to Collection");
            res.json("Success");
        }
      });
    })

//deleting datas
router.delete('/',(req, res, next)=>{
    var _id = req.param("id");
    //console.log(_id);
    Data.remove({_id : _id}, (err, result)=>{
        if(err){
            res.json("Error : " + err);
        }
        else{
            res.json("Succesfully deleted");
        }
    } );
})

//deleting datas
router.delete('/delete',(req, res, next)=>{
    var id = req.query.id;
    Data.remove({id : id}, (err, result)=>{
        if(err){
            res.json("Error : " + err);
        }
        else{
            res.json("Succesfully deleted");
        }
    } );
})

//deleting datas
router.delete('/delete_all',(req, res, next)=>{
    Data.remove({}, (err, result)=>{
        if(err){
            res.json("Error : " + err);
        }
        else{
            res.json("Succesfully deleted");
        }
    } );
})


//Fetch all entries by regex 
router.get('/get_latest_data', (req, res, next)=>{
    // var id = req.query.id;
    // console.log(id);
    Data.aggregate(
        [
            {$group: {
                "_id": "$id",
                "value" : {$last : "$value"},
                "logger_id" : {$last : "$logger_id"},
                "logger_name" : {$last : "$logger_name"},
                "entry_time" : {$last : "$_id"}
            }}
        ],(err, result)=>{
            if(err){
                res.json("Error : " + err);
            }
            else{
                for(i=0;i<result.length;i++){
                    res_entry = result[i];
                    res_entry.entry_time = res_entry.entry_time.getTimestamp();
                }
                res.json(result);
            }
        }
    ) ;
})

router.post('/get_latest_data_id', (req, res, next)=>{
    var l0_ids = JSON.parse(req.body.l0_ids);
    Data.find({id : { $in :   l0_ids }},
        ['_id','value','entry_time','logger_id','logger_name','id'], // Columns to Return
        {
            skip:0, // Starting Row
            limit: l0_ids.length, // Ending Row
            sort:{
                _id: -1 //Sort by Date Added DESC
            }
        }
    ,(err, datas)=>{
        for(i=0;i<datas.length;i++){
            data = datas[i];
            data.entry_time = data._id.getTimestamp();
            // console.log(data._id.getTimestamp())
        }
        res.json(datas);
    })
})


module.exports = router;
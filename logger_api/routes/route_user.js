const express = require('express');
const extend = require('extend');
const router = express.Router();

const User = require('../models/user');

// getting users
router.get('/', (req, res, next)=>{
    User.find((err, users)=>{
        res.json(users);
    })
});

//adding users
router.post('/',(req, res, next)=>{
    let newUser = new User(req.body);
    newUser.save((err, user)=>{
        if(err){
            //console.log(err);
            res.json("Error occured in saving : " + err);
        }
        else{
            res.json(user);
        }
    })
});

//updating structures
router.get('/update',(req, res, next)=>{
    console.log("Update function called");
    var id = req.query.id
    var user = req.query.user;
    var user = JSON.parse(user);
    // console.log(structure);
    User.findByIdAndUpdate(id, user, (err, result)=>{
        if(err){
            res.json("Error : " + err);
        }
        else{
            res.json("success");
        }
    })
    // res.json("success");
})

//updating structures
router.get('/add',(req, res, next)=>{
    var id = req.query.id
    var user = req.query.user;
    var user = JSON.parse(user);  
    let newUser = new User(user);
    newUser.save((err, user)=>{
        if(err){
            //console.log(err);
            res.json("Error occured in saving : " + err);
        }
        else{
            res.json("success");
        }
    })
})

router.get('/delete', (req, res, next)=>{
    var _id = req.query.id;
    User.remove({_id : _id}, (err, result)=>{
        if(err){
            res.json("Error : " + err);
        }
        else{
            res.json("success");
        }
    } );
})

router.get('/check_valid',(req, res, next)=>{
    let phone = req.query.phone;
    let password = req.query.password;
    User.findOne({phone:phone, password:password},(err, user)=>{
        if(err){
            res.json(err);
        }
        else{
            res.json(user);
        }
    })
});

router.post('/check_valid',(req, res, next)=>{
    let phone = req.body.phone;
    let password = req.body.password;
    User.findOne({phone:phone, password:password},(err, user)=>{
        if(err){
            res.json(err);
        }
        else{
            res.json(user);
        }
    })
});

router.post('/check_access',(req, res, next)=>{
    let phone = req.body.phone;
    User.findOne({phone:phone},(err, user)=>{
        if(err){
            res.json(err);
        }
        else{
            res.json(user);
        }
    })
});

//deleting users
router.delete('/',(req, res, next)=>{
    var _id = req.param("id");
    //console.log(_id);
    User.remove({_id : _id}, (err, result)=>{
        if(err){
            res.json("Error : " + err);
        }
        else{
            res.json("Succesfully deleted");
        }
    } );
})

router.delete('/delete_all',(req, res, next)=>{
    User.remove({}, (err, result)=>{
        if(err){
            res.json("Error : " + err);
        }
        else{
            res.json("Succesfully deleted");
        }
    } );
})

module.exports = router;
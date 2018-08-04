var express = require('express');
var router = express.Router();
var jwt = require('express-jwt');
var jwtSecretKey = "YourVeryveryJWTSecret"
var kafkaProducer = require('./kafkaProducer');
/* GET home page. */
router.get('/', function (req, res, next) {
  console.log(req.cookies['access-token']);
  res.json({
    message: "this is your message",
    id: 0001
  })
  res.end();
});

router.get('/nodejs/user/getUserProfile', jwt({
  secret: jwtSecretKey,
  getToken: function (req) {
    return req.cookies['access-token']
  }
}), function (req, res, next) {
  res.json(req.user);
  res.end();

})
router.post('/nodejs/user/sendKafkaMsg', function (req, res, next) {
  kafkaProducer.produce('thetopickey', req.body.message, function (result) {
    res.send(result);
    res.end();
  });
})

module.exports = router;

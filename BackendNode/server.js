
const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const cors = require('cors');


/** set up middleware */
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded());

// This is the router
const router = require('./routes')
router(app)



// Start your app.
app.listen(4000);

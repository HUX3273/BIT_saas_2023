let data

export function getData () {
    var json = require('./data.json');
    //console.log(json);
    data = json;
    return data
}

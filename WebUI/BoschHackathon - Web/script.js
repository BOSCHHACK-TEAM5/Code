var dom = document.getElementById("container");

var myChart = echarts.init(dom);
option = null;
var width = document.body.clientWidth/22;
var height = document.body.clientHeight/11;



//initialize 

var blockGrid = "1111111111111111111111111";
// var route = "0123012301230123012301230";
var route = "4444444444444444444444444";


var sensorData = [];

sensorData[0] = {
    noiselevel: -1,
    numPeople: -1,
    sensorID: 1,
    temperature: -1,
    xAccelData: -1,
    yAccelData: -1,
    zAccelData: -1
};

sensorData[1] = {
    noiselevel: -1,
    numPeople: -1,
    sensorID: 2,
    temperature: -1,
    xAccelData: -1,
    yAccelData: -1,
    zAccelData: -1
};


var grid = new Array(5); 
for (i = 0; i<5; i++ ) {
    grid[i] = [];
    for (j = 0; j < 5; j++) {
        grid[i].push([(7+2*j)*width,1.5*height+(2*i+1)*width])
    }
}

var geoCoordMap = {
    'p1' : grid[0][0],
    'p2' : grid[0][1],
    'p3' : grid[0][2],
    'p4' : grid[0][3],
    'p5' : grid[0][4],
    'p6' : grid[1][0],
    'p7' : grid[1][1],
    'p8' : grid[1][2],
    'p9' : grid[1][3],
    'p10' : grid[1][4],
    'p11' : grid[2][0],
    'p12' : grid[2][1],
    'p13' : grid[2][2],
    'p14' : grid[2][3],
    'p15' : grid[2][4],
    'p16' : grid[3][0],
    'p17' : grid[3][1],
    'p18' : grid[3][2],
    'p19' : grid[3][3],
    'p20' : grid[3][4],
    'p21' : grid[4][0],
    'p22' : grid[4][1],
    'p23' : grid[4][2],
    'p24' : grid[4][3],
    'p25' : grid[4][4]
};

var BJData = [
    {name:'p1',value: 0},
    {name:'p2',value: 0},
    {name:'p3',value: 0},
    {name:'p4',value: 0},
    {name:'p5',value: 0},
    {name:'p6',value: 0},
    {name:'p7',value: 0},
    {name:'p8',value: 0},
    {name:'p9',value: 0},
    {name:'p10',value: 0},
    {name:'p11',value: 0},
    {name:'p12',value: 0},
    {name:'p13',value: 0},
    {name:'p14',value: 0},
    {name:'p15',value: 0},
    {name:'p16',value: 0},
    {name:'p17',value: 0},
    {name:'p18',value: 0},
    {name:'p19',value: 0},
    {name:'p20',value: 0},
    {name:'p21',value: 0},
    {name:'p22',value: 0},
    {name:'p23',value: 0},
    {name:'p24',value: 0},
    {name:'p25',value: 0},
];

var color = '#a6c84c';
var series = [];
    series.push({
        type: 'lines',
        zlevel: 1,
        effect: {
            show: true,
            period: 1,
            trailLength: 0.7,
            color: '#fff',
            symbolSize: 3
        },
        lineStyle: {
            normal: {
                color: color,
                width: 0,
                curveness: 0
            }
        },
        data: convertData(route)
    },
    {
        type: 'effectScatter',
        coordinateSystem: 'geo',
        zlevel: 2,
        rippleEffect: {
            brushType: 'stroke'
        },
        label: {
            normal: {
                show: false,
            }
        },
        symbolSize: 9,
        itemStyle: {
            normal: {
                color: color
            }
        },
        data: BJData.map(function (dataItem) {
            return {
                name: dataItem.name,
                value: geoCoordMap[dataItem.name]
            }
        })
    },
    {
        type: 'effectScatter',
        coordinateSystem: 'geo',
        zlevel: 3,
        rippleEffect: {
            brushType: 'stroke'
        },
        label: {
            normal: {
                show: false,
            }
        },
        symbolSize: function(val){
            return  val[2]/2
        },
        itemStyle: {
            normal: {
                color: "#d31759"
            }
        },
        data: BJData.map(function (dataItem) {
            return {
                name: dataItem.name,
                value: geoCoordMap[dataItem.name].concat(dataItem.value)
            };
        })
        // data:  {
        //         name: "p13",
        //         value: geoCoordMap["p13"]   
        // }
    }
    );

option = {
    backgroundColor: '#404a59',
    // backgroundColor: 'transparent',
    title : {
        text: 'Crowd Channeling under Emergency',
        subtext: 'Bosch Hackthon Team 5',
        left: 'center',
        textStyle : {
            color: '#fff'
        }
    },

    geo: {
        map: '',
        label: {
            emphasis: {
                show: false
            }
        },
        roam: true,
        itemStyle: {
            normal: {
                areaColor: '#323c48',
                borderColor: '#404a59'
            },
            emphasis: {
                areaColor: '#2a333d'
            }
        }
    },
    series: series
};;
if (option && typeof option === "object") {
    myChart.setOption(option, true);
}


//Canvas drawing
var wid = document.body.clientWidth/22;
var heig = document.body.clientHeight/11;

var myCanvas = document.createElement('canvas');
myCanvas.id = 'newCanvas';
myCanvas.width = 2500;
myCanvas.height = 1000;
myCanvas.style.position = "absolute";
var innerDiv = document.querySelector('div > div');
innerDiv.appendChild(myCanvas);
var myCxt = myCanvas.getContext('2d');
myCxt.lineWidth = 10;
myCxt.strokeStyle='rgba(255,255,255,0.1)';  
for (i = 0; i < 5; i++){
    for (j = 0; j < 5; j++){
        xCoor = (6+2*j)*wid;
        yCoor = 1.5*heig+ 2*i*wid;
        myCxt.strokeRect(xCoor,yCoor,2*wid,2*wid);
    }
}


//add cube
var cubeList = new Array(5);
for (i = 0; i < 5; i++){ 
    cubeList[i] = [];
    for( j = 0; j < 5; j++){
        var cub = document.createElement('div')
        cub.classList.add("cub");

        cub.style.width = 2*wid - 10+ "px";
        cub.style.height = 2*wid - 10+ "px"; 
        cub.style.left = (6+2*j)*wid +5  + "px";
        cub.style.top = 1.5*heig + 2*i*wid +5  + "px";
        cub.onclick = cubClick;
        innerDiv.appendChild(cub);                    
        cubeList[i][j] = cub;        
    }
}

function cubClick() {
            if (!dragging1 && !dragging2){
                if (this.classList[1]) {
                    this.classList.remove("cubop");
                }
                else {
                    this.classList.add("cubop");
                }
            }
}
function block2String() {
    blockGrid = "";
    for (i = 0; i < 5; i++){
        for (j = 0; j < 5; j++){
            blockGrid += cubeList[i][j].classList.contains("cubop")? 0 : 1;
        }
    }
    return blockGrid;
}
block2String();

//add line
var lineList = [];
var lineThickness = "10px";
for (i = 0; i <25; i++){
    var lin1 = document.createElement('div');
    lin1.classList.add("lin");
    lin1.style.width = 2*wid - 10 + "px" ;
    lin1.style.height = lineThickness;
    lin1.style.left = (6 + 2 * (i % 5))*wid +5 + "px";
    lin1.style.top = 1.5 * heig + 2 * wid * Math.floor(i/5) - 5 + "px";
    lin1.onclick = linClick;
    innerDiv.appendChild(lin1);
    lineList.push(lin1);

    var lin2 = document.createElement('div');
    lin2.classList.add("lin");
    lin2.style.height = 2*wid - 10 + "px";
    lin2.style.width = lineThickness;
    lin2.style.left = (6 + 2 * (i % 5))*wid - 5 + "px";
    lin2.style.top = 1.5 * heig + 2 * wid * Math.floor(i/5) +5  + "px";
    lin2.onclick = linClick;    
    innerDiv.appendChild(lin2);
    lineList.push(lin2);   
}

for (i = 0; i <5; i++){
    var lin = document.createElement('div');
    lin.classList.add("lin");
    lin.style.width = 2*wid - 10 + "px";
    lin.style.height = lineThickness;
    lin.style.left = (6 + 2 * (i % 5))*wid +5 + "px";
    lin.style.top = 1.5 * heig + 2 * wid * 5 - 5 + "px";
    lin.onclick = linClick;    
    innerDiv.appendChild(lin);
    lineList.push(lin);
}

for (i = 0; i <5; i++){
    var lin = document.createElement('div');
    lin.classList.add("lin");
    lin.style.height = 2*wid - 10 + "px";
    lin.style.width = lineThickness;
    lin.style.left = (6 + 2 * 5 ) * wid + - 5 + "px";
    lin.style.top = 1.5 * heig + 2 * wid * i + 5 + "px";
    lin.onclick = linClick;    
    innerDiv.appendChild(lin);
    lineList.push(lin);
}

[0, 1, 2, 4, 6, 8, 11, 21, 31, 41, 55, 56, 57, 58, 59].forEach((item) => lineList[item].classList.add("linop"));


function linClick() {
    if (this.classList[1]) {
        this.classList.remove("linop");
    }
    else {
        this.classList.add("linop");
    }
}
var wall = "";

function wall2String() {
    wall = "";
    for (i = 0; i < lineList.length; i++){
        wall += lineList[i].classList.contains("linop")? "1" : "0";
    }
    return wall;
}
wall2String();

//begin hover

var camera1 = document.createElement('img');
var imgDiv1 = document.createElement('div');
camera1.src = 'camera.png';
camera1.classList.add('camera');
imgDiv1.id = "imgDiv1";
imgDiv1.classList.add("img-group");
imgDiv1.style.position = "absolute";
imgDiv1.style.top = document.body.clientHeight-250+"px";
imgDiv1.style.opacity = 1;
imgDiv1.appendChild(camera1);
innerDiv.appendChild(imgDiv1);


var camera2 = document.createElement('img');
var imgDiv2 = document.createElement('div');
camera2.src = 'camera.png';
camera2.classList.add('camera');
imgDiv2.id = "imgDiv2";
imgDiv2.classList.add("img-group");
imgDiv2.style.position = "absolute";
imgDiv2.style.top = document.body.clientHeight-150+"px";
imgDiv2.style.opacity = 1;
imgDiv2.appendChild(camera2);
innerDiv.appendChild(imgDiv2);


var textDiv1 = document.createElement('div');
textDiv1.classList.add("img-tip");
imgDiv1.appendChild(textDiv1);

var textDiv2 = document.createElement('div');
textDiv2.classList.add("img-tip");
imgDiv2.appendChild(textDiv2);

function setSensorText() {
    var s1 = sensorData[0];
    var s2 = sensorData[1]; 
    textDiv1.innerHTML = "Noise: "+ s1.noiselevel + "<br/>People: " + s1.numPeople + "<br/>sensorID:"+ s1.sensorID + "<br /> Temp:"+ s1.temperature + "<br /> xAccel:" + s1.xAccelData + "<br/> yAccel:" + s1.yAccelData + "<br/> zAccel:" + s1.zAccelData;
    textDiv2.innerHTML = "Noise: "+ s2.noiselevel + "<br/>People: " + s2.numPeople + "<br/>sensorID:"+ s2.sensorID + "<br /> Temp:"+ s2.temperature + "<br /> xAccel:" + s2.xAccelData + "<br/> yAccel:" + s2.yAccelData + "<br/> zAccel:" + s2.zAccelData;
}
setSensorText();

//begin dragging

var dragging1 = false;
var dragging2 = false;

imgDiv1.onmousedown = down;
imgDiv2.onmousedown = down;
document.onmousemove = move;
document.onmouseup = up;

function getMouseXY(e) {
  var x = 0, y = 0;
  e = e || window.event;
    x = e.clientX + document.body.scrollLeft - document.body.clientLeft;
    y = e.clientY + document.body.scrollTop - document.body.clientTop;
  return {
    x: x,
    y: y
  };
}


function down(e) {
  if (this.id == "imgDiv1") {
    dragging1 = true;
  }
  else {
      dragging2 = true;
  }
  
  cameraX = this.offsetLeft;
  cameraY = this.offsetTop;
  mouseX = parseInt(getMouseXY(e).x);
  mouseY = parseInt(getMouseXY(e).y);
  offsetX = mouseX - cameraX;
  offsetY = mouseY - cameraY;
}

function move(e) {
    if (dragging1) {
        var x = getMouseXY(e).x - offsetX;
        var y = getMouseXY(e).y - offsetY;
        var width = document.documentElement.clientWidth - imgDiv1.offsetWidth;
        var height = document.documentElement.clientHeight - imgDiv1.offsetHeight;

        x = Math.min(Math.max(0, x), width);
        y = Math.min(Math.max(0, y), height);
        imgDiv1.style.left = x + 'px';
        imgDiv1.style.top = y + 'px';
    }
    if (dragging2) {
        var x = getMouseXY(e).x - offsetX;
        var y = getMouseXY(e).y - offsetY;
        var width = document.documentElement.clientWidth - imgDiv2.offsetWidth;
        var height = document.documentElement.clientHeight - imgDiv2.offsetHeight;

        x = Math.min(Math.max(0, x), width);
        y = Math.min(Math.max(0, y), height);
        imgDiv2.style.left = x + 'px';
        imgDiv2.style.top = y + 'px';
    }

}

function up(e) {
  dragging1 = false;
  dragging2 = false;
}

var sensor1Pos = -1;
var sensor2Pos = -1;

function getCamera1Grid() {
    var divX = imgDiv1.offsetLeft + 40;
    var divY = imgDiv1.offsetTop + 30;
    console.log("X:"+ divX + "Y:" +divY);
    if (divX >= wid*6 && divX <= wid*16){
        if (divY >= heig*3 && divY <= heig*3 + wid*10){
            var hori = Math.floor(divX/(2*wid)) - 2;
            console.log("hori"+hori);
            var verti = Math.floor((divY-1.5*heig)/(2*wid))+1;
            console.log("verti"+verti);     
            sensor1Pos = (verti-1)*5 + hori - 1;       
            return sensor1Pos ;               
        }        
    }
    return -1;
}

function getCamera2Grid() {
    var divX = imgDiv2.offsetLeft + 50;
    var divY = imgDiv2.offsetTop + 30;
    console.log("X:"+ divX + "Y:" +divY);
    if (divX >= wid*6 && divX <= wid*16){
        if (divY >= heig*3 && divY <= heig*3 + wid*10){
            var hori = Math.floor(divX/(2*wid)) - 2;
            console.log("hori"+hori);
            var verti = Math.floor((divY-1.5*heig)/(2*wid))+1;
            console.log("verti"+verti);     
            sensor2Pos = (verti-1)*5 + hori - 1;       
            return sensor2Pos ;               
        }        
    }
    return -1;
}

//refresh
var refreshButton = document.createElement('button');
refreshButton.onclick = refresh;
refreshButton.innerText = "Refresh";
refreshButton.type = "button";
refreshButton.style.top = "200px";
innerDiv.appendChild(refreshButton);


function refresh() {
    block2String();
    wall2String();
    [route, mapColor] = getRoute();
    getSensorData(0);
    getSensorData(1);
    setSensorText();
    setMapColor();
    series[0].data= convertData(route);
    if (option && typeof option === "object") {
        option.series = series;
        myChart.setOption(option, true);
    }
}

function setMapColor() {
    mapColor.split("").forEach(function (item,i) {
        BJData[i].value = parseInt(item) * 60;
    })
    series[2].data = BJData.map(function (dataItem) {
            return {
                name: dataItem.name,
                value: geoCoordMap[dataItem.name].concat(dataItem.value)
            };
    });
}


function convertData(route) {
    var arr = route.split("");
    for (i = 0; i < 25 ; i++){
        if (blockGrid[i] === "1"){
        switch(arr[i]) {
            case "0":
                if (i - 4 < 0){
                    BJData[i].des = BJData[i].name;                    
                }
                else {
                    BJData[i].des = "p"+ (i-4);                    
                }
                break;
            case "1":
                if ((i + 1) % 5 == 0){
                    BJData[i].des = BJData[i].name;                                        
                }
                else {
                    BJData[i].des = "p"+ (i+2);            
                }
                break;
            case "2":
                if (i + 6 > 25){
                    BJData[i].des = BJData[i].name;                    
                }
                else {
                    BJData[i].des = "p"+ (i+6);                                                
                }
                break;
            case "3":
                if ( i % 5 == 0){
                    BJData[i].des = BJData[i].name;                                        
                }
                else {
                    BJData[i].des = "p"+ i;            
                }
                break;
            case "4":
                BJData[i].des = BJData[i].name;                                        
                break;
        }
    }
    else{
        BJData[i].des = BJData[i].name;                                        
    }
    }
    var res = [];
    for (var i = 0; i < BJData.length; i++) {
        var dataItem = BJData[i];
        var toCoord = geoCoordMap[dataItem.des];
        var fromCoord = geoCoordMap[dataItem.name];
        if (fromCoord && toCoord) {
            res.push({
                fromName: dataItem.name,
                toName: dataItem.des,
                coords: [fromCoord, toCoord]
            });
        }
    }
    return res;
};


//post 

function getSensorData(i){
    console.log("Post!");
    var xhr = new XMLHttpRequest();
    var url = "http://139.219.229.71:3030/webapp?id="+i;
    xhr.open('POST',url,true);
    xhr.setRequestHeader('content-type', 'application/json');
    xhr.send(null);
    xhr.onreadystatechange = function(){
        console.log("readyState:"+xhr.readyState+"status:"+xhr.status);
        if (xhr.readyState == 4 && xhr.status == 200) {
            console.log("Request successful!");
	        sensorData[i] = JSON.parse(xhr.responseText);	
	    }
        if(xhr.readyState == 4 && xhr.status == 0){
            console.log("No response from server!");
        }
    };
}
var mapColor;
function getRoute(){
    console.log("Get route!");
    var envData = {
        grid: block2String(),
        wall: wall2String(),
        sensor1: getCamera1Grid(),
        sensor2: getCamera2Grid()
    };

    var xhr = new XMLHttpRequest();
    var url = "http://139.219.229.71:3030/webapp?id=-1&grid="+blockGrid+"&sensor1="+sensor1Pos+"&sensor2="+sensor2Pos+"&wall="+wall;
    xhr.open('GET',url,true);
    xhr.setRequestHeader('content-type', 'application/json');
    xhr.send(null);
    xhr.onreadystatechange = function(){
        console.log("readyState:"+xhr.readyState+"status:"+xhr.status);
        if (xhr.readyState == 4 && xhr.status == 200) {
            console.log("Request successful!");
	        var responseObj = JSON.parse(xhr.responseText);	
                console.log(xhr.responseText);
                    route = responseObj.path;
                    mapColor = responseObj.color;
                }
	    }
        if(xhr.readyState == 4 && xhr.status == 0){
            console.log("No response from server!");
        };
        return [route, mapColor];
    }






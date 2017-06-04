from flask import Flask, request
from flask import current_app, jsonify
from flask_cors import CORS
import time
import json
import MySQLdb
import math

app = Flask(__name__)
CORS(app)

#grid = ""
#wall = ""


table_name = 'sensorinfo'
def get_acc(x, y, z):
    xAcc = x
    yAcc = y
    zAcc = z
    max_t = max(x, y, z)
    if abs(x/float(max_t)) < 0.25: xAcc = 0
    if abs(y/float(max_t)) < 0.25: yAcc = 0
    if abs(z/float(max_t)) < 0.25: zAcc = 0
    if 900 <= z or z <= -900: zAcc = 0
    result = math.sqrt(xAcc**2+yAcc**2+zAcc**2)
    return result if result > 1000 else 0 

def compute(grid, wall, sensor1, sensor2, data):
    sensor1 = int(sensor1)
    sensor2 = int(sensor2)
    color_grid = ['0' for i in range(25)]
    ob = [1 for i in range(25)]
    dist = [[1000000 for j in range(25)] for i in range(25)]
    direct = [[0 for j in range(25)] for i in range(25)]
    finaldir = [0 for i in range(25)]
    get_temp = lambda x: x if x > 50 else 0 
    for i in range(25):
        #print data[0]
        ob[i] += (1 - int(grid[i])) * 1000000;
        if i == sensor1:
            ob[i] += get_temp(data[0][5]) * 1 + int(data[0][6]) / 30000 * 1 + get_acc(int(data[0][1]), int(data[0][2]), int(data[0][3])) * 1 + int(data[0][4]) / 5 * 1
            print ob[i]
        if i == sensor2:
            ob[i] += get_temp(data[1][5]) * 1 + int(data[1][6]) / 30000 * 1 + get_acc(int(data[1][1]), int(data[1][2]), int(data[1][3])) * 1 + int(data[1][4]) / 5 * 1
            print ob[i]
    for i in range(25):
        if ob[i] != 1000000 and ob[i] > 50  and (i == sensor1 or i == sensor2):
            color_grid[i] = '2'
            if wall[i * 2] == '0':
                if i >=5 and color_grid[i-5] != '2': 
                    color_grid[i-5] = '1'
            if wall[i * 2 + 1] == '0':
                if i % 5 != 0  and color_grid[i-1] != '2': color_grid[i-1] = '1'
            if wall[(i + 5) * 2] == '0':
                if i <= 19: color_grid[i+5] = '1'
            if wall[(i + 1) * 2 + 1] == '0':
                if (i+1) % 5 != 0: color_grid[i+1] = '1'

    for i in range(25):
        dist[i][i] = 0
        if grid[i] == '0':
            continue
        if i >= 5 and grid[i - 5] != '0' and wall[i * 2] == '0':
            dist[i][i - 5] = ob[i - 5]
            direct[i][i - 5] = 0
        if i % 5 >= 1 and grid[i - 1] != '0' and wall[i * 2 + 1] == '0':
            dist[i][i - 1] = ob[i - 1]
            direct[i][i - 1] = 3
        if i < 20 and grid[i + 5] != '0' and wall[(i + 5) * 2] == '0':
            dist[i][i + 5] = ob[i + 5]
            direct[i][i + 5] = 2
        if i % 5 < 4 and grid[i + 1] != '0' and wall[(i + 1) * 2 + 1] == '0':
            dist[i][i + 1] = ob[i + 1]
            direct[i][i + 1] = 1

    for k in range(25):
        for i in range(25):
            for j in range(25):
                if i != j and j != k and i != k and dist[i][k] < 1000000 and dist[k][j] < 1000000 and grid[i] and grid[j] and grid[k]:
                    if (dist[i][k] + dist[k][j] < dist[i][j]):
                        dist[i][j] = dist[i][k] + dist[k][j] 
                        direct[i][j] = direct[i][k]
    escape = []
    for i in range(5):
        if wall[i * 2] == '0':
            escape.append(i)
        if wall[i * 5 * 2 + 1] == '0':
            escape.append(i * 5)
        if wall[50 + i] == '0':
            escape.append(20 + i)
        if wall[55 + i] == '0':
            escape.append(i * 5 + 4)
    #print 'escape', escape
    for i in range(25):
        if grid[i] == '1' and not(i in escape):
            min = 1000000000;
            minn = -1;
            for j in escape:
                print i, dist[i][j]
                if dist[i][j] < min:
                    min = dist[i][j]
                    minn = j
            finaldir[i] = direct[i][minn]
        elif i in escape:
            finaldir[i] = 4
    finalans = ""
    for i in range(25):
        finalans += str(finaldir[i])
    return finalans, color_grid
    
    



@app.route('/webapp', methods=["GET", "POST"])
#@crossdomain(origin='*')
def sensor():
    #print('AAA')
    values = request.args
    #print('aaa')
    #resp = make_response("hello") 
    #resp.headers['Access-Control-Allow-Origin'] = '*'
    conn = MySQLdb.connect(
        host='localhost',
        port=3306,
        user='root',
        passwd='123456',
        db='map',
    )
    cur = conn.cursor()
    cur.execute("select * from %s" % (table_name))
    data = cur.fetchall()
    print data
    cur.close()
    conn.close()
    if values.get('id') == '-1':
        #for item in data:
        #    sensorID, xAccelData, yAccelData, zAccelData, numPeople, temperature, noiselevel = item
            #print sensorID
        #print compute(web.input().grid, web.input().wall, web.input().sensor1, web.input().sensor2, data)
        path, color = compute(values.get('grid'), values.get('wall'), values.get('sensor1'), values.get('sensor2'), data)
        testreturn = {
                'path': path, 'color': ''.join(color)
        }
        print testreturn
        return json.dumps(testreturn)
    else:
        sensorID, xAccelData, yAccelData, zAccelData, numPeople, temperature, noiselevel = data[int(values.get('id'))]
        return_value = {'sensorID':sensorID, 'xAccelData':int(xAccelData), 'yAccelData':int(yAccelData), 'zAccelData':int(zAccelData), 'numPeople':int(numPeople), 'temperature':temperature, 'noiselevel':noiselevel}
        print return_value
        return json.dumps(return_value)

    #for item in data:
    #    sensorID, xAccelData, yAccelData, zAccelData, numPeople, temperature, noiselevel = item
    #print compute(web.input().grid, web.input().wall, web.input().sensor1, web.input().sensor2, data)
    #testreturn = {
    #'path': compute(values.get('grid'), values.get('wall'), values.get('sensor1'), values.get('sensor2'), data)
    #}
    #print testreturn
    #return json.dumps(testreturn)
    #return jsonify(foo='cross domain ftw') 


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=3030)
    cur.close()
    conn.close()

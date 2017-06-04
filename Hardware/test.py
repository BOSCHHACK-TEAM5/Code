import http.client
import urllib.request
import urllib.parse
import urllib.error
import base64
import json
import picamera
import time
import requests

url_latest = 'http://42.159.133.122:8082/mongodb/boschxdk23/latestdata'
url_recent = 'http://42.159.133.122:8082/mongodb/boschxdk23/recentdata'
url_all = 'http://42.159.133.122:8082/mongodb/boschxdk23/alldata'
url_server = 'http://139.219.229.71:80/pi'


def SensorData():
    response = requests.get(url_latest)
    return response.json()


def CameraData():
    camera = picamera.PiCamera()
    camera.capture('image.jpg', quality=70)
    #time.sleep(1)
    camera.close()


def FaceCount(filename):
    headers = {
        # Request headers
        'Content-Type': 'application/octet-stream',
        'Ocp-Apim-Subscription-Key': 'db2c19cd8ba44ce0bda9da02aae27b9a',
    }

    params = urllib.parse.urlencode({
        # Request parameters
        'returnFaceId': 'true',
        'returnFaceLandmarks': 'false',
        'returnFaceAttributes': 'age,gender',
    })
    item_dict = {}
    try:
        body = ""
        f = open(filename, "rb")
        body = f.read()
        f.close()
        conn = http.client.HTTPSConnection('api.cognitive.azure.cn')
        conn.request("POST", "/face/v1.0/detect?%s" % params, body, headers)
        response = conn.getresponse()
        data = response.read().decode('utf-8')
        item_dict = json.loads(data)
        # print(len(item_dict))
        #data['numPeople'] = len(item_dict)
        #requests.post(url_server, data)
        # print(data)
        conn.close()
    except Exception as e:
        print("[Errno {0}] {1}".format(e.errno, e.strerror))
    return len(item_dict)

cnt = 0
while True:
    data = SensorData()
    CameraData()
    num = FaceCount('image.jpg')
    data['numPeople'] = num
    data['cameraid'] = 1
    #print(cnt)
    cnt += 1
    #time.sleep(1)
    try:    
        response = requests.post(url_server, data)
        print(cnt)
    except:
        cnt -= 1
        continue
    time.sleep(1)
    #response = requests.get(url_server)

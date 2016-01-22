#!/usr/bin/python
import logging
import requests
import cjson
import re
import random
import simplejson as json
from StringIO import StringIO
from jsonpath_rw import jsonpath, parse

logging.basicConfig(level=logging.DEBUG)

# define a Handler which writes INFO messages or higher to the sys.stderr
console = logging.StreamHandler()
console.setLevel(logging.INFO)
# set a format which is simpler for console use
formatter = logging.Formatter('%(asctime)s - %(message)s')
# tell the handler to use this format
console.setFormatter(formatter)

logging.info('Generating UUIDS for organisation.')

organisations_response = requests.get('http://ftaps39403-law1a-eu-t/transformers/organisations', stream=True)
# uuids_json = cjson.decode(organisations_response.text)
# api_url_jpath = parse('.[].apiUrl')

most_annotated = open('src/test/resources/organisation/most_annotated.uuid', 'r')
most_relationships = open('src/test/resources/organisation/most_relationships.uuid', 'r')
result_file = open('src/test/resources/organisation/organisations.uuid', 'w')
result_file.write('%s\n' % 'uuid')

def getMostRelationships(rnd_int, most_file, out_file):
    if (random_int == 7 or random_int == 6):
        most_uuid = most_file.readline()
        if most_uuid != '':
            out_file.write('%s' % most_uuid)
        else:
            most_file = open('src/test/resources/organisation/most_relationships.uuid', 'r')

def getMostAnnodated(rnd_int, most_file, out_file):
    if (random_int == 9 or random_int == 4):
        most_uuid = most_file.readline()
        if most_uuid != '':
            out_file.write('%s' % most_uuid)
        else:
            most_file = open('src/test/resources/organisation/most_annotated.uuid', 'r')


# for url in api_url_jpath.find(uuids_json):
for obj in json.load(StringIO(organisations_response.content)):
    if random.randint(0, 4) == 0:
        random_int = int(random.gauss(7, 2))
        getMostRelationships(random_int, most_relationships, result_file)
	getMostAnnodated(random_int, most_annotated, result_file)
        if (random_int == 8 or random_int == 5):
            uuid_re = re.search('[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}', obj['apiUrl'])
            result_file.write('%s\n' % uuid_re.group(0))

most_uuid = most_annotated.readline()

while most_uuid != '':
    result_file.write('%s' % most_uuid)
    most_uuid = most_annotated.readline()

result_file.close()
most_relationships.close()
most_annotated.close()
logging.info('Done')

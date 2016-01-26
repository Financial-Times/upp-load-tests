#!/usr/bin/python
import logging
import random
import re
import requests
import simplejson as json
from StringIO import StringIO

logging.basicConfig(level=logging.DEBUG)

# define a Handler which writes INFO messages or higher to the sys.stderr
console = logging.StreamHandler()
console.setLevel(logging.INFO)
# set a format which is simpler for console use
formatter = logging.Formatter('%(asctime)s - %(message)s')
# tell the handler to use this format
console.setFormatter(formatter)

logging.info('Generating UUIDS for people.')

people_response = requests.get('http://ftaps35629-law1a-eu-t/transformers/people', stream=True)
# uuids_json = cjson.decode(organisations_response.text)
# api_url_jpath = parse('.[].apiUrl')

most_memberships = open('src/test/resources/people/people_top_members_1000.uuid', 'r')
most_popular = open('src/test/resources/people/people_most_popular.uuid', 'r')
result_file = open('src/test/resources/people/people.uuid', 'w')
result_file.write('%s\n' % 'uuid')


def getMost(rnd_int, most_file, most_file_path, out_file, left_int, right_int):
    if (random_int == left_int or random_int == right_int):
        most_uuid = most_file.readline()
        if most_uuid != '':
            out_file.write('%s' % most_uuid)
        else:
            most_file = open(most_file_path, 'r')


def getMostPouplar(rnd_int, most_file, out_file):
    getMost(rnd_int, most_file, 'src/test/resources/people/people_most_popular.uuid', out_file, 6, 7)


def getMostMemberships(rnd_int, most_file, out_file):
    getMost(rnd_int, most_file, 'src/test/resources/people/people_top_members_1000.uuid', out_file, 4, 9)


# for url in api_url_jpath.find(uuids_json):
for obj in json.load(StringIO(people_response.content)):
    if random.randint(0, 4) == 0:
        random_int = int(random.gauss(7, 2))
        getMostPouplar(random_int, most_popular, result_file)
        getMostMemberships(random_int, most_memberships, result_file)
        if (random_int == 8 or random_int == 5):
            uuid_re = re.search('[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}', obj['apiUrl'])
            result_file.write('%s\n' % uuid_re.group(0))

most_uuid = most_memberships.readline()

while most_uuid != '':
    result_file.write('%s' % most_uuid)
    most_uuid = most_memberships.readline()

result_file.close()
most_popular.close()
most_memberships.close()
logging.info('Done')

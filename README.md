# README #
N.B. This is still under development.

## Abstract ##
This is a home project to create simple irrigation system based on Raspberry Pi.

## Prerequisites ##
1. Google event should be 0 minutes long

## Method ##
1. Runs as a service
2. Checks google calendar in order to get next date for 'calendar_eventId' once in a 'check_period_seconds' to reschedule next watering
3. Sets 'pump_gpio' port in a high mode for 'pump_delay_seconds' when watering happens
4. Sends email to 'notify_email' each time watering occurs

## Configuration ##
Here's example /usr/local/pirrigation/conf/pirrigation.conf:
    check_period_seconds = 1440 # once a day
    calendar_eventId = water
    pump_gpio = 25
    pump_delay_seconds = 5
    notify_email = 'test@test.com'

## Running Instruction ##
1. [ Create SSH ](http://www.linuxproblem.org/art_9.html) key and copy public key on Pi
2. Put private key in ssh_keys/pi_rsa
3. Run gradle deploy

## Google calendar configuration ##
1. Create service account
2. Copy service account key to conf folder
3. Share calendar with service account using https://calendar.google.com. Service account email can be found in https://console.developers.google.com/permissions/serviceaccounts?project=PROJECT_ID
4. N.B. Calendar id is account email now!
5. Find event id using https://developers.google.com/google-apps/calendar/v3/reference/events/list with q as a parameter

## Tested ##
Tested on Raspberry Pi 2 Model B.

## Links ##
1. [ Pi4J ](http://pi4j.com/)
2. [ Pin numbering for Pi 2 Model B ](http://pi4j.com/pins/model-2b-rev1.html)
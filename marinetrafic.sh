#!/bin/bash
while (true)
do
	java MarineTraficCrawler fishingships.csv
        java MarineTraficCrawler ships2.csv
	sleep 86400

done

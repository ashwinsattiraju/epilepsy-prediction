cd ../data
for i in $(ls -AF | grep /); do
	cd $i
	./convert.sh
	cd ..
done
cd ../converter
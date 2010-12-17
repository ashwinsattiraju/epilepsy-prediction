for i in $(ls *.edf); do
	./edf2ascii.link $i
done
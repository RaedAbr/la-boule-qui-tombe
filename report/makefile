FLAGS = -shell-escape

all: report.pdf mostlyclean

report.pdf: report.tex
	pdflatex $(FLAGS) report.tex
	pdflatex $(FLAGS) report.tex

mostlyclean:
	rm -rf *.log *.aux *.out _minted-report

clean:
	rm -rf *.pdf *.log *.aux *.out _minted-report

rebuild: clean all

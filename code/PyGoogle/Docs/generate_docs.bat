RMDIR /S /Q "Common/latex"
RMDIR /S /Q "Common/html"
RMDIR /S /Q "Crawler/latex"
RMDIR /S /Q "Crawler/html"
RMDIR /S /Q "Indexer/latex"
RMDIR /S /Q "Indexer/html"
RMDIR /S /Q "WebUI/latex"
RMDIR /S /Q "WebUI/html"
RMDIR /S /Q "Test/latex"
RMDIR /S /Q "Test/html"
doxygen pygoogle-common-config.dox
doxygen pygoogle-indexer-config.dox
doxygen pygoogle-crawler-config.dox
doxygen pygoogle-webui-config.dox
doxygen pygoogle-test-config.dox
cd Common/latex
make pdf
cd ../Crawler/latex
make pdf
cd ../Indexer/latex
make pdf
cd ../WebUI/latex
make pdf
cd ../Test/latex
make pdf
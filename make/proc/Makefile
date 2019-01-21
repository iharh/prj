.PHONY: build ar bn en hi

build: ar bn en hi
	@echo build finished

ar: ; $(MAKE) lp LP_ID="ar" LP_VER="1"
bn: ; $(MAKE) lp LP_ID="bn" LP_VER="2"
en: ; $(MAKE) lp LP_ID="en" LP_VER="3"
hi: ; $(MAKE) lp LP_ID="hi" LP_VER="4"

lp: ; @echo lp called with LP_ID: $(LP_ID) LP_VER: $(LP_VER)

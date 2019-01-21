.PHONY: build bn en

build: bn en
	@echo build called

bn: ; $(MAKE) lp LP_ID="bn" LP_VER="1"

en: ; $(MAKE) lp LP_ID="en" LP_VER="2"

lp:
	@echo lp called with LP_ID: $(LP_ID) LP_VER: $(LP_VER)

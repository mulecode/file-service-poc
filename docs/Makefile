$(VERBOSE).SILENT:
.DEFAULT_GOAL := help

PUML_FILES := $(wildcard *.puml)

####################################################################################
##@ Rendering
####################################################################################

.PHONE: render
render: ## Render all puml files to svg
	pwd
	for file in $(PUML_FILES); do \
		echo "Rendering $$file"; \
		plantuml -tsvg $$file -o ./img;\
	done

####################################################################################
##@ Utils
####################################################################################
.PHONY: help
help: ## Display this help
	@awk \
	  'BEGIN { \
	    FS = ":.*##"; printf "\nUsage:\n"\
			"  make \033[36m<target>\033[0m\n" \
	  } /^[a-zA-Z_-]+:.*?##/ { \
	    printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2 \
	  } /^##@/ { \
	    printf "\n\033[1m%s\033[0m\n", substr($$0, 5) \
	  } ' $(MAKEFILE_LIST)

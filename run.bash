#!/bin/sh

gen_sample_schema() {
	(cd samples/schema-maven-usage && exec mvn -Pgenerate compile)
}

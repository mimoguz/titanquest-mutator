mvn clean package
jpackage --name TQMutator \
         --type app-image \
         --description "Titan Quest AE savegame editor" \
         --module-path "target/classes" \
         --module TQMutator/io.github.mimoguz.tqmutator.Main \
         --dest target/
ln -s -r target/TQMutator/bin/TQMutator target/TQMutator/tqmutator
tar -C target -zcvf target/tqmutator.tar.gz TQMutator/
        


package pl.edu.agh.school.guice;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import pl.edu.agh.logger.FileMessageSerializer;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.persistence.PersistenceManager;
import pl.edu.agh.school.persistence.SerializablePersistenceManager;

public class SchoolModule extends AbstractModule {

    @Provides
    PersistenceManager providePersistenceManager(SerializablePersistenceManager serializablePersistenceManager) {
        return serializablePersistenceManager;
    }

    @Provides
    @Named("teachersStorageFileName")
    String provideTeachersStorageFileName() {
        return "guice-teachers.dat";
    }

    @Provides
    @Named("classStorageFileName")
    String provideClassesStorageFileName() {
        return "guice-classes.dat";
    }

    @Provides
    @Singleton
    Logger provideLogger() {
        FileMessageSerializer fileMessageSerializer = new FileMessageSerializer("persistence.log");
        return new Logger(fileMessageSerializer);
    }
}


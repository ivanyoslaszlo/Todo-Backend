package laszlo.dev.todo.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class Mylogger {

    private File dir = new File("mylogs");

    public Mylogger() {

        if (!dir.exists()) {
            dir.mkdirs();
        }

    }


    public boolean writer (String filename,String content){
        try {

            FileWriter writer= new FileWriter(new File(dir,filename),true);

            LocalDateTime originalTime = LocalDateTime.now();
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss");
            String formatedDate=formatter.format(originalTime);

            writer.write(formatedDate+" - "+content+"\n");
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("Hiba a file irásakor!");
            return false;
        }

    }

    public boolean info(String info) {

        return writer("info.log",info);

    }

    public boolean warn(String warn) {

     return writer("warn.log",warn);


    }

    public boolean error(String error){

        return writer("error.log",error);


    }
}

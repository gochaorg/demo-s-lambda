package org.example.lazyprop;

import java.io.IOException;

public interface Observer<T> {
    public void beginRead(Prop<T> prop);
    public void endRead(Prop<T> prop, T value);

    public void beginWrite(Prop<T> prop, T value);
    public void endWrite(Prop<T> prop, T value);

    public static class Logger<T> implements Observer<T> {
        private int level=0;
        private final Appendable out;
        private int readNested = 0;
        private int writtenNested = 0;
        private int readingNested = 0;
        private int writingNested = 0;
        private boolean resetOnTop = true;
        private boolean statOnTop = true;

        public Logger(Appendable out){
            if( out==null )throw new IllegalArgumentException( "out==null" );
            this.out = out;
        }

        private String indent(boolean read,boolean begin){
            return "| "+
                (
                    begin
                        ? read ? "->" : "=>"
                        : read ? "<-" : "<="
                ).repeat(level+1)+" ";
        }
        private final String reading ="reading ";
        private final String read    ="read    ";
        private final String writing ="writing ";
        private final String written ="written ";

        private void stat(){
            try {
                out.append("| ").append("stat:").append(System.lineSeparator());
                out.append("|     ").append(Integer.toString(readNested)).append(" reads").append(System.lineSeparator());
                out.append("|     ").append(Integer.toString(writtenNested)).append(" written").append(System.lineSeparator());
                out.append(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void reset(){
            readNested = 0;
            writtenNested = 0;
            readingNested = 0;
            writingNested = 0;
        }

        private void onTopRead(){
            if( statOnTop )stat();
            if( resetOnTop )reset();
        }
        private void onTopWritten(){
            if( statOnTop )stat();
            if( resetOnTop )reset();
        }

        @Override
        public void beginRead(Prop<T> prop) {
            readingNested++;
            var name = NamedProp.nameOf(prop).orElse("?");
            try {
                out
                    .append(indent(true, true))
                    .append(reading)
                    .append(name)
                    .append(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
            level++;
        }

        @Override
        public void endRead(Prop<T> prop, T value) {
            readNested++;
            level--;
            var name = NamedProp.nameOf(prop).orElse("?");
            try {
                out.append(indent(true, false))
                    .append(read)
                    .append(name)
                    .append(" = ")
                    .append(value!=null ? value.toString() : "null")
                    .append(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if( level==0 ) onTopRead();
        }

        @Override
        public void beginWrite(Prop<T> prop, T value) {
            writingNested++;
            var name = NamedProp.nameOf(prop).orElse("?");
            try {
                out
                    .append(indent(false, true))
                    .append(writing)
                    .append(name)
                    .append(" = ")
                    .append(value!=null ? value.toString() : "null")
                    .append(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
            level++;
        }

        @Override
        public void endWrite(Prop<T> prop, T value) {
            writtenNested++;
            level--;
            var name = NamedProp.nameOf(prop).orElse("?");
            try {
                out.append(indent(false, false))
                    .append(written)
                    .append(name)
                    .append(" = ")
                    .append(value!=null ? value.toString() : "null")
                    .append(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if( level==0 ) onTopWritten();
        }
    }

    public static Logger loggerAny = new Logger<>(System.out);
    public static <T> Logger<T> logger(){
        Logger<T> log = loggerAny;
        return log;
    }
}

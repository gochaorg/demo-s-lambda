package org.example.lazyprop;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SampleComputeTest {
    @Test
    public void test01(){
        var firstName0 = new MutProp<>("Ivan");
        var firstName =
            CallCountProp.of(firstName0)
                .onReading(x -> System.out.println("reading firstName"))
                .onWriting(x -> System.out.println("writing firstName"));
        var lastName0 = new MutProp<>("Petrov");
        var lastName =
            CallCountProp.of(firstName0)
                .onReading(x -> System.out.println("reading lastName"))
                .onWriting(x -> System.out.println("writing lastName"));

        var nameCnt = new AtomicInteger(0);

        var name0 = ComputeProp.eval(
            firstName,
            lastName,
            (fname, lname) ->
            {
            nameCnt.incrementAndGet();
            return fname+" "+lname;
        });
        var name =
            CallCountProp.of(name0)
                .onReading(x -> System.out.println("reading name"))
                .onWriting(x -> System.out.println("writing name"));

        var age0 = new MutProp<>(12);
        var age = CallCountProp.of(age0)
            .onReading(x -> System.out.println("reading age"))
            .onWriting(x -> System.out.println("writing age"));

        var template0 = new MutProp<>("${name} поздравляем с ${age} годами жизни на этой планете");
        var template =
        CallCountProp.of(template0)
            .onReading(x -> System.out.println("reading template"))
            .onWriting(x -> System.out.println("writing template"));

        var messageCnt = new AtomicInteger(0);
        var message0 = ComputeProp.eval(
            name, age, template, (nameValue,ageValue,tmpl) -> {
                messageCnt.incrementAndGet();
                return tmpl
                    .replace("${age}", ageValue.toString())
                    .replace("${name}", nameValue);
            });
        var message =
            CallCountProp.of(message0)
                .onReading(x -> System.out.println("reading message"))
                .onWriting(x -> System.out.println("writing message"));

        Consumer<Runnable> statCapture = code -> {
            System.out.println("-".repeat(30));
            nameCnt.set(0);
            messageCnt.set(0);
            code.run();
            System.out.println("\nstat:");
            System.out.println(
                "sum="+(nameCnt.get()+messageCnt.get())+" name.c="+nameCnt+" message.c="+messageCnt
            );
            System.out.println("-".repeat(30));
        };

        statCapture.accept( ()->System.out.println(message.get()) );

        statCapture.accept( ()->{
            template.set("О! ${name} вам уже ${age} лет? поздравляю!");
            System.out.println(message.get());
        });

        statCapture.accept( ()->{
            firstName.set("Peter");
            System.out.println(message.get());
        });
    }

    @Test
    public void test01_clean1(){
        var firstName = new MutProp<>("Ivan");
        var lastName = new MutProp<>("Petrov");

        var nameCnt = new AtomicInteger(0);
        var name = ComputeProp.eval(
            firstName,lastName, (fname, lname) ->{
            nameCnt.incrementAndGet();
            return fname+" "+lname;
        });

        var age = new MutProp<>(12);

        var template = new MutProp<>("${name} поздравляем с ${age} годами жизни на этой планете");

        var messageCnt = new AtomicInteger(0);
        var message = ComputeProp.eval(
            name, age, template, (nameValue,ageValue,tmpl) -> {
                messageCnt.incrementAndGet();
                return tmpl
                    .replace("${age}", ageValue.toString())
                    .replace("${name}", nameValue);
            });

        Consumer<Runnable> statCapture = code -> {
            System.out.println("-".repeat(30));
            nameCnt.set(0);
            messageCnt.set(0);
            code.run();
            System.out.println("\nstat:");
            System.out.println(
                "sum="+(nameCnt.get()+messageCnt.get())+" name.c="+nameCnt+" message.c="+messageCnt
            );
            System.out.println("-".repeat(30));
        };

        statCapture.accept( ()->System.out.println(message.get()) );

        statCapture.accept( ()->{
            template.set("О! ${name} вам уже ${age} лет? поздравляю!");
            System.out.println(message.get());
        });

        statCapture.accept( ()->{
            firstName.set("Peter");
            System.out.println(message.get());
        });
    }

    @Test
    public void test01_clean2(){
        var firstName = new MutProp<>("Ivan");
        var lastName = new MutProp<>("Petrov");

        var name = ComputeProp.eval(firstName,lastName, (fname, lname) -> fname+" "+lname);

        var age = new MutProp<>(12);

        var template = new MutProp<>("${name} поздравляем с ${age} годами жизни на этой планете");

        var message = ComputeProp.eval(
            name, age, template, (nameValue,ageValue,tmpl) -> tmpl
                    .replace("${age}", ageValue.toString())
                    .replace("${name}", nameValue));

        System.out.println(message.get());

        template.set("О! ${name} вам уже ${age} лет? поздравляю!");
        System.out.println(message.get());

        firstName.set("Peter");
        System.out.println(message.get());
    }

    @Test
    public void test01_log(){
        var firstName = new MutProp<>("Ivan").named("firstName").observable(Observer.logger());
        var lastName = new MutProp<>("Petrov").named("lastName").observable(Observer.logger());

        var name = ComputeProp
            .eval(firstName,lastName, (fname, lname) -> fname+" "+lname)
            .named("name").observable(Observer.logger());

        var age = new MutProp<>(12)
            .named("age").observable(Observer.logger());

        var template = new MutProp<>("${name} поздравляем с ${age} годами жизни на этой планете")
            .named("template").observable(Observer.logger());

        var message = ComputeProp.eval(
            name, age, template, (nameValue,ageValue,tmpl) -> tmpl
                    .replace("${age}", ageValue.toString())
                    .replace("${name}", nameValue))
            .named("message").observable(Observer.logger());

        System.out.println(message.get());

        template.set("О! ${name} вам уже ${age} лет? поздравляю!");
        System.out.println(message.get());

        firstName.set("Peter");
        System.out.println(message.get());
    }

    @Test
    public void test02(){
        var firstName = new MutProp<>("Ivan");
        var lastName = new MutProp<>("Petrov");

        var name = ComputeProp.eval( () -> firstName.get()+" "+lastName.get() );

        var age = new MutProp<>(12);

        var template = new MutProp<>("${name} поздравляем с ${age} годами жизни на этой планете");

        var message = ComputeProp.eval(
            () -> template.get()
                    .replace("${age}", age.get().toString())
                    .replace("${name}", name.get()));

        System.out.println(message.get());

        template.set("О! ${name} вам уже ${age} лет? поздравляю!");
        System.out.println(message.get());

        firstName.set("Peter");
        System.out.println(message.get());
    }

    @Test
    public void test02_log(){
        var firstName = new MutProp<>("Ivan").named("firstName").observable(Observer.logger());
        var lastName = new MutProp<>("Petrov").named("lastName").observable(Observer.logger());

        var name = ComputeProp
            .eval( () -> firstName.get()+" "+lastName.get() )
            .named("name").observable(Observer.logger());

        var age = new MutProp<>(12);

        var template = new MutProp<>("${name} поздравляем с ${age} годами жизни на этой планете")
            .named("template").observable(Observer.logger());

        var message = ComputeProp.eval(
            () -> template.get()
                    .replace("${age}", age.get().toString())
                    .replace("${name}", name.get()))
            .named("message").observable(Observer.logger());

        System.out.println(message.get());

        template.set("О! ${name} вам уже ${age} лет? поздравляю!");
        System.out.println(message.get());

        firstName.set("Peter");
        System.out.println(message.get());
    }
}

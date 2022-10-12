@param app.Person person
@param String message

<html>
    <head>
        <title>Person: ${person.name()}</title>
    </head>
    <style>
        @import url("/css/main.css");
    </style>
    <body>
        <h1>${message}</h1>
        <p>
            <img width="500" src="/people/images/${person.name()}" />
        </p>
    </body>
</html>
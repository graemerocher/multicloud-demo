@param String name
@param Long id

<html>
    <head>
        <title>Person: ${name}</title>
    </head>
    <style>
        body {
            background-image: url('/images/cloudworld-bg.png');
            background-size: 100%;

        }
        h1 {
            padding-top: 300px;
            padding-left: 180px;
            font-family: "Helvetica Neue", serif;
        }
        p {
            padding-left: 200px;
            font-family: "Helvetica Neue", serif;
        }
    </style>
    <body>
        <h1>Hello, ${name}!</h1>
        <p>
            <img width="500" src="/people/images/${id}" />
        </p>
    </body>
</html>
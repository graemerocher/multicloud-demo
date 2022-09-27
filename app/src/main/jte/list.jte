@param Iterable<app.Person> people


<html>
<head>
    <title>People</title>
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
    ol {
        padding-left: 200px;
        font-family: "Helvetica Neue", serif;
    }
</style>
<body>
<h1>List of People:</h1>
<ol>
    @for(app.Person person : people)
        <li><a href="/people/${person.id()}">${person.name()}</a></li>
    @endfor
</ol>
</body>
</html>

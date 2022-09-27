@param Iterable<app.Person> people


<html>
<head>
    <title>People</title>
</head>
<body>
<h1>List of People:</h1>
<ol>
    @for(app.Person person : people)
        <li><a href="/people/${person.getId()}">${person.getName()}</a></li>
    @endfor
</ol>
</body>
</html>

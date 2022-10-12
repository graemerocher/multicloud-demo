@param Iterable<app.Person> people


<html>
<head>
    <title>People</title>
</head>
<style>
    @import url("/css/main.css");
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

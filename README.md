# java-filmorate

## Диаграмма БД

![](diagram.png)

### Описание диаграммы для java-filmorate

- Таблицы **films** / **users** исходные.
- Таблица **films_like** хранит список лайков пользователей для каждого фильма;
- Таблица **friends_list** хранит информацию о списке друзей и статусе дружбы;
- Таблица **mpa_rate** хранит список возможных рейтингов;
- Таблица **genre** хранит всех список возможных жанров;

### Пример запросов к БД
Обновление FILM 

```sql 
UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, genre = ?, mpa_id = ?
WHERE id = ?
```

Поиск USER по id
```sql 
SELECT * FROM users WHERE id = ?
```

create table if not exists alumnos(

  dniAlumno text primary key not null,
  nombres text, apellidos text,
  sexo text, NroMadre text,
  NroPadre text,
  promo text,
  idcurso integer,
  foreign key(idcurso) references cursos(id_curso) on delete set null
);
create table if not exists profesores(
  dniprofesor text primary key not null,
  nombres text,
  apellidos text,
  sexo text,
  NroContacto text
);

create table if not exists cursos(
  id_curso integer primary key autoincrement,
  bachiller text,
  seccion text,
  grado text
);
create table if not exists horarios(
  id_horario integer primary key autoincrement,
  horario_inicio text,
  horario_fin text
);

create table if not exists materias(
  id_materia integer primary key autoincrement,
  nombre text
);
create table if not exists asistenciaunion(
  idunion integer primary key autoincrement,
  fecha text,
  idcursofk int,
  foreign key (idcursofk) references cursos(id_curso) on delete cascade
);

create table if not exists asistenciaAlumno(
  id_asistencia integer primary key autoincrement,
  id_alumno text,
  idunion int,
  foreign key (id_alumno) references alumnos(dniAlumno) on delete cascade,
  foreign key (idunion) references asistenciaunion(idunion) on delete cascade
);
create table if not exists presenciaAlumno(
  idpresencia integer primary key autoincrement,
  idhorario int,
  id_asistencia integer,
  presencia text,
  foreign key (id_asistencia) references asistenciaAlumno(id_asistencia) on delete cascade,
  foreign key (idhorario) references horarios(id_horario) on delete cascade
);
create table if not exists asistenciaprofe(
  id_asistencia integer primary key autoincrement,
  id_profesor text,
  idunion text,
  foreign key (id_profesor) references profesores(dniprofesor) on delete cascade,
  foreign key (idunion) references asistenciaunion(idunion) on delete cascade
);
create table if not exists presenciaprofesor(
  id_presencia integer primary key autoincrement,
  idhorario int,
  idmateria int,
  id_asistencia int,
  presencia text,
  foreign key (idhorario) references horarios(id_horario) on delete cascade,
  foreign key (idmateria) references materias(id_materia) on delete cascade,
  foreign key (id_asistencia) references asistenciaprofe(id_asistencia) on delete cascade
);

create table if not exists users(
  iduser integer primary key autoincrement,
  username text,
  password text
);
PRAGMA foreign_keys=on
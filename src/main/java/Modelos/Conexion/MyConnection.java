/*
 * Derechos de autor 2019 Rodolfo Elias Ojeda Almada
 *
 * Licenciado bajo la Licencia Apache, Versión 2.0 (la "Licencia");
 * no puede utilizar este archivo, excepto en cumplimiento con la Licencia.
 * Puede obtener una copia de la licencia en
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * A menos que sea requerido por la ley aplicable o acordado por escrito, software
 * distribuido bajo la Licencia se distribuye "TAL CUAL",
 * SIN GARANTÍAS O CONDICIONES DE NINGÚN TIPO, ya sea expresa o implícita.
 * Consulte la Licencia para el idioma específico que rige los permisos y
 * Limitaciones bajo la Licencia.
 */

package Modelos.Conexion;

import java.sql.Connection;
import java.sql.SQLException;

public interface MyConnection{
    Connection conect() throws SQLException;
}

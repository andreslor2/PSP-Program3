package conexion.BD;

import gestor.documental.GestorDocumental;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import proceso.principal.ListDocumentos;
import javax.swing.JOptionPane;
;

/**
 *
 * @author andres.velez
 */
public class ConnectOracle 
{
    private Connection con;
    public static ConnectOracle instancia;
    private static final GestorDocumental DOCUMENTOS = new GestorDocumental();
    private PreparedStatement prepareStatement;
    private transient boolean conectado;
    String texto1;

    public void setConectado ( final boolean sconectado) 
    {
        conectado = sconectado;
    }

    public boolean isConectado ( ) 
    {
        return conectado;
    }
    
    public ConnectOracle ( )
    {
       try 
        {
//            Class.forName(DOCUMENTOS.leerProperties("driver"));
//            final String URL = DOCUMENTOS.leerProperties("URL");
//            final String username = DOCUMENTOS.leerProperties("Username");
//            final String password = DOCUMENTOS.leerProperties("Password");
//            con = DriverManager.getConnection(URL, username, password);
            setConectado(true);
        }
      catch (Exception e) 
      {
       try 
          {
            con.close();
          } 
        catch (SQLException ex) 
        {
            setConectado(false);
        }
        setConectado(false);
      }
    }
    
 public String DatosApp ( final int indicador,final ListDocumentos ListDoc)
    {
        final StringBuilder respondo =new StringBuilder();
        float suma =0;
        double sumacuadrado=0,xy=0,sumaXY=0,YK,XK;
        float suma2 =0,B1=0,B0=0,gamaXY=0,gamaCuadrado=0;
        double suma2cuadrado=0;
        float media =0;
        float media2 =0;
//        float desviacion =0;

        
        int n =0;
     try
      {
        final List<String> listDoc = ListDoc.getDocumento();
        n = listDoc.size();
        for(int i=0;i<listDoc.size();i++)
         {
           listDoc.get(i);
           final String Temp1=listDoc.get(i).toString().split(";")[0];
           final String Temp2=listDoc.get(i).toString().split(";")[1];
           xy= Float.parseFloat(Temp1)*Float.parseFloat(Temp2);
           sumaXY= sumaXY + xy;
           suma = suma + Float.parseFloat(Temp1);
           suma2 = suma2 + Float.parseFloat(Temp2);
           sumacuadrado= sumacuadrado +  Math.pow((Float.parseFloat(Temp1)),2);
           suma2cuadrado= suma2cuadrado +  Math.pow((Float.parseFloat(Temp2)),2);
 
         }
         media=suma/listDoc.size();
         media2=suma2/listDoc.size();
         B1= (float)((sumaXY - (n * media * media2))/((sumacuadrado)-(n*(media*media))));
//         texto1= "el valor de B1 es: "+ B1+"\n";
//         JOptionPane.showMessageDialog(null, texto1);
         gamaXY=(float) (((n*sumaXY)-(suma*suma2))/Math.sqrt(((n*sumacuadrado)-(suma*suma))*((n*suma2cuadrado)-(suma2*suma2))));
         gamaCuadrado = gamaXY * gamaXY;
         B0= media2 - (B1*media);
         XK=386;
         YK= B0 + (B1*XK);
         //desviacion=CalculoDesviacion(media,ListDoc);
         respondo.delete(0,respondo.length());
         respondo.append("000,"+B0+","+B1+","+gamaXY+","+gamaCuadrado+","+n+","+XK+","+YK);
         System.out.println("\n La suma es: "+suma);
         System.out.println("\n La media es: "+media);
         System.out.println("\n La suma de las Y es: "+suma2);
         System.out.println("\n La media de las Y es: "+media2);
       }
        catch(Exception ex)
         {
           respondo.delete(0, respondo.length());
           respondo.append("000"); //Error la insertar registros en base de datos 
         }
      return respondo.toString();
    }
  
  
  private String consultas ( final int indicador) 
  {
        final StringBuilder retorno = new StringBuilder();
        switch (indicador)
        {
           case 1:                  
            final String add2 ="DELETE FROM ADMINPRUEBAS.T_DOCUMENTOS";
            retorno.append(add2);
          break;
          case 2:                  
            final String add ="INSERT INTO ADMINPRUEBAS.T_DOCUMENTOS (DOCUMENTO) VALUES(?)";
            retorno.append(add);
          break;
          
        }
        return retorno.toString();
    }

    
   public  String obtenerHoraFinal ( ) 
    {
      Date fecha = new Date();  
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(fecha);	
      calendar.add(Calendar.HOUR, 2); 
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.mmm");
      final String HoraFinal = sdf.format(calendar.getTime());
      return HoraFinal;
    }  
   
   public float CalculoDesviacion ( float media,final ListDocumentos ListDoc)
   {
   float retorno=0;
   float suma=0;
   float antes=0;
   try
      {
        final List<String> listDoc = ListDoc.getDocumento();
        for(int i=0;i<listDoc.size();i++)
         {
           listDoc.get(i);
           suma=suma+ ((Float.parseFloat(listDoc.get(i))-media)*(Float.parseFloat(listDoc.get(i))-media));
           
         }
         antes=suma/(listDoc.size()-1);
         retorno=(float) Math.sqrt(antes);
         //respondo.delete(0,respondo.length());
         //respondo.append("000,"+suma+","+media+","+desviacion);
         }
        catch(Exception ex)
         {
           //respondo.delete(0, respondo.length());
           //respondo.append("000"); //Error la insertar registros en base de datos 
         }
   
   return retorno;
   }

}

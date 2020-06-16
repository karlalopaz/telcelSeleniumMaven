package telcel;


import org.junit.Test;

public class TelcelSeleccionarArticulo extends TelcelParent
{

    @Test
    public void Telcel ()
    { //mandar llamar a los metodos siguientes
        navegarSitio("https://www.telcel.com");
        verificarLandingPage();
        listarTelefonos();
        seleccionarEstado("Jalisco");
        verificarPaginaResultados();
        Celular primerCelular = capturarDatosCelular(1);
        seleccionarCelular(1);
        validarDatosCelular(primerCelular);
        cerrarBrowser();
    }
}

package telcel;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class TelcelParent
{
 WebDriver driver;
 WebDriverWait wait;

    public void navegarSitio(String url)
    {
        driver = new ChromeDriver(); //abrir el navegador
        driver.manage().window().maximize(); //maximizar la pantalla
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //hacer espera de 30 seg y cada que usas el driver, se usa en todas las
        //instrucciones, modificas el comportamiento del driver.
        wait = new WebDriverWait(driver, 10);
        driver.navigate().to(url); // ir a la url
    }
    public void verificarLandingPage()
    {
        // verificar que existan logoTelcel, tiendaEnLinea, campoBusqueda y asegurarnos que esta visible

        WebElement logoTelcel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[src='/content/dam/htmls/img/icons/logo-telcel.svg']")));
        WebElement tiendaEnLinea = driver.findElement(By.cssSelector("[data-nombreboton='Tienda en linea superior']"));
        WebElement campoBusqueda = driver.findElement(By.cssSelector("#buscador-menu-input"));

        if (logoTelcel.isDisplayed() && tiendaEnLinea.isDisplayed() && campoBusqueda.isDisplayed()) {
            System.out.println("Pagina principal de telcel carga perfecto"); // si estan visibles, imprime mensaje
        } else {
            System.out.println("no carga la pagina"); //si no esta visible, imprime mensaje
            System.exit(-1); //sale del programa y regrea un -1 para marcar un error.
        }

    }
    public void listarTelefonos()
    {
        //busca elementos tienda en linea, y link de telefonos celulares
        WebElement tiendaEnLinea = driver.findElement(By.cssSelector("[data-nombreboton='Tienda en linea superior']"));
        tiendaEnLinea.click();
        WebElement linkTelefonosCelulares = driver.findElement(By.cssSelector(".shortcut-container [data-nombreboton='Telefonos y smartphones']"));
        linkTelefonosCelulares.click();

    }
    public void seleccionarEstado(String nombreEstado)
    {

        System.out.println("breakpoint para verificar la pagina"); //imprime mensaje para verificar la pagina
        //busca elemento modal para seleccionar estado
        WebElement seleccionaEstadoDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".modal .chosen-single")));
        seleccionaEstadoDropdown.click();

        //busca elementos: text box, opcion del estado y boton de entrar
        WebElement textBoxEstado = driver.findElement(By.cssSelector(".chosen-search input"));
        textBoxEstado.sendKeys(nombreEstado);
        WebElement opcionEstado = driver.findElement(By.cssSelector(".chosen-results li")); //selecciona el estado correcto
        opcionEstado.click();
        WebElement botonEntrar = driver.findElement(By.id("entrarPerfilador"));
        botonEntrar.click(); //da click en el boton de entrar
    }

    public void verificarPaginaResultados()
    {
        //busca elementos celulares y los guarda en una lista
        WebElement esperaCargaCel = wait.until((ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".comp-telcel-mosaico-equipos li"))));
        List<WebElement> celulares = driver.findElements(By.cssSelector(".comp-telcel-mosaico-equipos li"));
        esperaCargaCel = null;
        System.out.println(celulares.size());
        if (celulares.size() > 1) {
            System.out.println("La lista se desplego correctamente"); //imprime si la lista de celulares se desplego correctamente
        }
    }

    public Celular capturarDatosCelular(int i)
    {
        //busca el elemento marcaModelo, nombre, precio y capacidad

        WebElement textoMarcaModelo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".telcel-mosaico-equipos-marca")));
        String mm = textoMarcaModelo.getText(); //guarda el elemento en una variable

        WebElement textoNombre = driver.findElement(By.cssSelector(".telcel-mosaico-equipos-nombre-equipo"));
        String nombreEquipo = textoNombre.getText(); //guarda el elemento en una variable


        WebElement textoPrecio = driver.findElement(By.cssSelector(".telcel-mosaico-equipos-precio"));
        String precioEquipo = textoPrecio.getText(); //guarda el elemento en una variable
        precioEquipo = precioEquipo.replace(",", ""); //reemplaza la coma del precio por un nada
        precioEquipo = precioEquipo.replace("$", ""); //reemplaza el $ del precio por un nada
        double pe = Double.parseDouble(precioEquipo); //convierte el precio a double y lo guarda en una variable


        WebElement textoCapacidad = driver.findElement(By.cssSelector(".telcel-mosaico-equipos-capacidad-numero"));
        String capacidadEquipo = textoCapacidad.getText();// guarda el elemento en una variable
        String[] datos = capacidadEquipo.split(" "); //separa la capacidad por el espacio y lo guarda en un string
        String capacidadString = datos[0]; //la variable guarda los datos de la posicion cero del arreglo
        int numGigas = Integer.parseInt(capacidadString); //convierte el valor a int y lo guarda en una variable


        return new Celular(mm, nombreEquipo, pe, numGigas); //regresa los valores
    }

    public void seleccionarCelular(int numeroCelular) {
        //busca el elemento celulares y lo guarda en una lista
        List<WebElement> celulares = driver.findElements(By.cssSelector(".comp-telcel-mosaico-equipos li"));
        System.out.println(celulares.size()); //imprime el tamaño de la lista
        WebElement celular = celulares.get(numeroCelular - 1); //nos posicionamos en el elemento marcado en la variable numeroCelular
        celular.click(); //da click y selecciona el celular correcto
    }

    public void validarDatosCelular(Celular primerCelular)
    {
        //buscar los elementos de marca modelo, nombre, precio y capacidad en la pagina del celular
        WebElement textoMarcaModelo = driver.findElement(By.cssSelector(".ecommerce-ficha-tecnica-opciones-compra #ecommerce-ficha-tecnica-modelo"));
        String mm = textoMarcaModelo.getText(); //guarda el elemento en una variable
        // compara los datos de los celulares
        if (primerCelular.getMarcaModelo().equals(mm)) {
            System.out.println("Marca y modelo son correctos");
        } else
        {
            System.out.println("Marca y modelo son distintos");
        }

        WebElement textoNombre = driver.findElement(By.cssSelector(".ecommerce-ficha-tecnica-opciones-compra #ecommerce-ficha-tecnica-nombre"));
        String nombreEquipo = textoNombre.getText(); //guarda el elemento en una variable
        if (primerCelular.getNombre().equals(nombreEquipo)){
            System.out.println("El nombre del equipo es correcto");
        }
        else
        {
            System.out.println("El nombre del equipo no es correcto");
        }


        WebElement textoPrecio = driver.findElement(By.cssSelector(".ecommerce-ficha-tecnica-opciones-compra #ecommerce-ficha-tecnica-precio-obj"));
        String precioEquipo = textoPrecio.getText(); //guarda el elemento en una variable
        precioEquipo = precioEquipo.replace(",", ""); //reemplaza la coma del precio por un nada
        precioEquipo = precioEquipo.replace("$", ""); //reemplaza el $ del precio por un nada
        double pe = Double.parseDouble(precioEquipo); //convierte el precio a double y lo guarda en una variable
        if (primerCelular.getPrecio() == pe)
        {
            System.out.println("El precio es correcto");
        }
        else
        {
            System.out.println("El precio no es correcto");
        }


        WebElement textoCapacidad = driver.findElement(By.cssSelector(".ecommerce-ficha-tecnica-opciones-compra .ecommerce-ficha-tecnica-opciones-compra-caracteristicas-etiqueta"));
        String capacidadEquipo = textoCapacidad.getText();// guarda el elemento en una variable
        String[] datos = capacidadEquipo.split(" "); //separa la capacidad por el espacio y lo guarda en un string
        String capacidadString = datos[0]; //la variable guarda los datos de la posicion cero del arreglo
        int numGigas = Integer.parseInt(capacidadString); //convierte el valor a int y lo guarda en una variable
        if (primerCelular.getCapacidadGb() == numGigas)
        {
            System.out.println("La capacidad es correcta");
        }
        else
        {
            System.out.println("La capacidad no es correcta");
        }
    }
    public void cerrarBrowser()
    {
        driver.quit();
    }
}


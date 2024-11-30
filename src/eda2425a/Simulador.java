// Versi�n 3 del Simulador para la Segunda Pr�ctica
package eda2425a;

public class Simulador {
    
    public static long NOPER;  // Contador del n�mero de operaciones

    public static class VecXY {
        public static VecXY Zero = new VecXY(0,0);
        
        public float x, y;
        
        public VecXY(float x, float y) { this.x = x; this.y = y; }
        
        public void Add(VecXY otro) { x += otro.x; y += otro.y; NOPER += 2; }
        
        public void Scale(float k) { x *= k; y *= k; NOPER += 2; }
    }   
    
    public class Goticula {
        public float x, y;     // Posici�n
        public float vx, vy;   // Velocidad        
        public float xa, ya;   // Posici�n anterior
        public VecXY fp;       // Fuerza debida al diferencial de presi�n
        public VecXY fu;       // Fuerza debida a la interacci�n del usuario
        public VecXY fv;       // Fuerza debida a la viscosidad        
        public float d;        // Densidad en la posici�n de la got�cula        

        public void SetPos(float xn, float yn, int lx, int ly) {
            x = xn; y = yn;
            if (x < 0) { x = 0; vx *= -0.95f; NOPER++; }
            if (y < 0) { y = 0; vy *= -0.95f; NOPER++; }
            if (x >= lx) { x = lx-0.00001f; vx *= -0.95f; NOPER += 2; }
            if (y >= ly) { y = ly-0.00001f; vy *= -0.95f; NOPER += 2; }
        }

        @Override
        public String toString() {
            return String.format("pos:[%.3f;%.3f] vel:[%.3f;%.3f]",x,y,vx,vy);
        }       
    }
    
    // Par�metros
    public float DT = 0.005f;               // Paso de integraci�n
    public float D0 = 6.0f;                 // Densidad objetivo
    public float KP = 1000.0f;              // Conversi�n fuerza-presi�n
    public float KV = 0.5f;                 // Conversi�n fuerza-viscosidad
    public float KU = 100.0f;               // Factor interacci�n usuario
    public float GX = 0.0f;                 // Gravedad (x)
    public float GY = -9.81f;               // Gravedad (y)      
    static float KK = (float) (6/Math.PI);  // Normalizaci�n del Kernel

    // Variables
    public Goticula[] gotas;   // Las got�culas
    public int n;              // N�mero de got�culas
    public int lx;             // Longitud del tanque
    public int ly;             // Altura del tanque
    public double tpo;         // Tiempo de la �ltima ejecuci�n (milisegundos)
    public float xu, yu;       // Posici�n del rat�n (xu < 0 si no se aplica)
    public float ru;           // Radio fuerza usuario
    public float su;           // Signo intensidad fuerza usuario

    /**
     * Atenci�n: En vuestro constructor debeis incluir la llamada a �ste 
     * (mediante super()) o dara error de compilaci�n. Deber�ais a�adir las
     * operaciones de creaci�n e inicializaci�n de las estructuras de datos de
     * apoyo
     */   
    public Simulador(int n) {
        this.n = n;
        xu = -1;
        CalculaDimensiones();  // <- Nuevo! Se calcula lx e ly antes de llamar a CreaGoticulas
        CreaGoticulas();
        ColocaGoticulasEnCuadrado();
    }   
    
    /**
     * Atenci�n: Si creais una clase que extienda a Goticula entonces deb�is
     * sobrescribir (override) �ste m�todo en vuestra clase para que al crear
     * las got�culas sean objetos de la clase extendida. Ver ejemplo depositado
     * en el Campus Virtual
     */
    public void CreaGoticulas() {
        // Creamos y colocamos las got�culas
        gotas = new Goticula[n];
        for(int i = 0; i < n; i++) { gotas[i] = new Goticula(); }       
    }

    /**********************************************************************
     ************************ ZONA NO MODIFICABLE *************************
     *********************************************************************/

    /**
     * Calcula las dimensiones (lx, ly) del tanque
     */
    public final void CalculaDimensiones() {
        // Calculamos las dimensiones adecuadas del tanque
        int nfil = (int) Math.floor(Math.sqrt(n));
        int ncol = (n-1)/nfil + 1;
        float sep = (float) Math.sqrt(n/(nfil*ncol*D0));
        ly = (int) Math.ceil((nfil+1)*sep);
        lx = 16*ly/9;
        ru = ly/5f;
    }
    
    /**
     * Dispone las got�culas en la configuraci�n inicial
     */
    public final void ColocaGoticulasEnCuadrado() {
        int nfil = (int) Math.floor(Math.sqrt(n));
        int ncol = (n-1)/nfil + 1;
        float sep = (float) Math.sqrt(n/(nfil*ncol*D0));
        int i = 0;
        for(Goticula g: gotas) {
            float x = sep*(i % ncol + 1);
            float y = sep*(i / ncol + 1);
            g.SetPos(x+0.0001f*y, y-0.0001f*x, lx, ly);
            i++;
        }        
    }

    /**
     * Devuelve la distancia entre los puntos (x1,y1) y (x2,y2)
     * @param x1    La posici�n x del primer punto
     * @param y1    La posici�n y del primer punto
     * @param x2    La posici�n x del segundo punto
     * @param y2    La posici�n y del segundo punto
     * @return  La distancia euclidea entre los puntos
     */    
    protected final float Dist(float x1, float y1, float x2, float y2) {
        NOPER += 8;
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }
    
    /**
     * Funci�n de ponderaci�n (kernel)
     * @param d Distancia
     * @return  El valor de ponderaci�n a esa distancia
     */    
    protected final float Kernel(float d) {
        if(d <= 1) { NOPER += 4; }
        return d > 1 ? 0 : KK*(1-d)*(1-d);
    }
 
    /**
     * Derivada de la funci�n de ponderaci�n
     * @param d Distancia
     * @return  La derivada de la funci�n a esa distancia
     */
    protected final float DKernel(float d) {
        if(d <= 1) { NOPER += 3; }
        return d > 1 ? 0 : KK*2*(d-1);
    }   
    
    /**
     * Calcula la contribuci�n de cada got�cula a la densidad en el punto (x,y)
     * @param x Posici�n x del punto
     * @param y Posici�n y del punto
     * @param g Got�cula que se considera
     * @return La contribuci�n de la got�cula a la densidad
     */
    protected final float CalcDensidadIter(float x, float y, Goticula g) {
        NOPER++; // Tenemos en cuenta aqu� la suma que se realiza en CalcDensidad
        return Kernel(Dist(x, y, g.x, g.y));
    }

    /**
     * Devuelve la fuerza de presi�n que ejerce una got�cula sobre otra
     * @param gi    Got�cula objetivo
     * @param gj    Got�cula que ejerce la fuerza
     * @return      Vector de fuerza de la presi�n de gj sobre gi
     */
    protected final VecXY CalcPresionIter(Goticula gi, Goticula gj) {
        if(gi == gj) { return VecXY.Zero; }
        // Distancia entre goticulas (atenci�n: se usa la posici�n salvada)
        float dist = Dist(gi.xa, gi.ya, gj.xa, gj.ya);
        // Si las part�culas estan muy juntas puede dar problemas num�ricos
        if(dist < 0.0001f) { return VecXY.Zero; }
        NOPER += 13;
        // Diferencial de densidad en la otra part�cula
        float dj = gj.d;
        float fr = KP*0.5f*((gi.d-D0)+(dj-D0))*DKernel(dist)/dj;
        // Devolvemos la fuerza
        return new VecXY(fr*(gj.xa-gi.xa)/dist, fr*(gj.ya-gi.ya)/dist);
    }

    /**
     * Devuelve la fuerza viscosa que ejerce una got�cula sobre otra
     * @param gi    Got�cula objetivo
     * @param gj    Got�cula que ejerce la fuerza
     * @return      Vector de fuerza viscosa de gj sobre gi
     */    
    protected final VecXY CalcViscosidadIter(Goticula gi, Goticula gj) {
        if(gi == gj) { return VecXY.Zero; }
        // Distancia entre goticulas (atenci�n: se usa la posici�n salvada)
        float dist = Dist(gi.xa, gi.ya, gj.xa, gj.ya);
        if(dist < 0.0001f) { return VecXY.Zero; }
        NOPER += 7;
        // Si las part�culas estan muy juntas puede dar problemas num�ricos
        float fr = KV*Kernel(dist);
        // Cada got�cula debe tender a tener la misma velocidad que sus vecinas
        return new VecXY(fr*(gj.vx - gi.vx)/dist, fr*(gj.vy - gi.vy)/dist);
    }
    
    /**
     * Calcula la fuerza debida a la interacci�n del usuario.
     * @param g La got�cula sobre la que se calcula la fuerza
     * @return La fuerza (fx,fy) que se realiza sobre la got�cula. Se devuelve
     * en un vector (x,y) (clase VecXY).
     */    
    protected final VecXY CalcInteraccion(Goticula g) {
        if(xu < 0) { return VecXY.Zero; }
        // Distancia entre la goticula y el punto marcado por el usuario
        float dist = Dist(xu, yu, g.x, g.y);
        if(dist > ru) { return VecXY.Zero; }
        float dx = 0, dy = 0; // Direcci�n al punto, normalizada
        if(dist > 0.0001f) { 
            NOPER += 4;
            dx = (xu - g.x)/dist;
            dy = (yu - g.y)/dist;
        }
        NOPER += 10;
        float fr = 1 - dist/ru;
        return new VecXY(fr*(su*KU*dx - g.vx), fr*(su*KU*dy - g.vy));
    }
        
    public final void Click(float x, float y, boolean implosion) {
        xu = x; yu = y; su = implosion ? -1 : 1;        
    }
    
    public final void NoClick() {
        xu = -1;
    }

    /**
     * Realiza un paso de la simulaci�n, integrando el sistema un intervalo
     * de tiempo DT. Se actualizan las posiciones y velocidades de las 
     * got�culas (array gotas).
     * Se utiliza el m�todo de Euler con alg�n que otro truco para mejorar la
     * estabilidad de la simulaci�n.
     */
    public final void PasoSimulacion() {
        NOPER = 0;
        long t0 = System.nanoTime();
        // Aplicamos la fuerza de la gravedad y predecimos la nueva posici�n
        for(Goticula g: gotas) {
            // Salvamos la posici�n anterior
            g.xa = g.x; g.ya = g.y;
            NOPER += 8;
            // Aplicamos la gravedad
            g.vx += DT*GX; g.vy += DT*GY;
            // Actualizamos la posici�n
            g.SetPos(g.x + DT*g.vx, g.y + DT*g.vy, lx, ly);
        }
        // Adaptaci�n de vuestras estructuras de datos al cambio de posici�n de las got�culas
        ReestructuraED();
        // Precalculamos y almacenamos la densidad
        for(Goticula g: gotas) { 
            g.d = CalcDensidad(g.x, g.y);
        }
        // Calculamos la fuerza debida a los diferenciales de presi�n,
        // la viscosidad y la interacci�n del usuario        
        for(Goticula g: gotas) {
            g.fp = CalcPresion(g);
            g.fu = CalcInteraccion(g);
            g.fv = CalcViscosidad(g);
        }
        // Aplicamos las fuerzas calculadas y almacenadas en el bucle anterior        
        for(Goticula g: gotas) {
            NOPER += 12;
            g.vx += DT*(g.fp.x + g.fu.x + g.fv.x);
            g.vy += DT*(g.fp.y + g.fu.y + g.fv.y);
            // Actualizamos la posici�n (usamos la posici�n original)
            g.SetPos(g.xa + DT*g.vx, g.ya + DT*g.vy, lx, ly);
        }
        tpo = 1e-6*(System.nanoTime()-t0);
    }
    
    
    /**********************************************************************
     ******************** ZONA MODIFICABLE POR HERENCIA *******************
     *********************************************************************/
    
    /**
     * Calcula la densidad en los alrededores del punto (x,y).
     * Recorre todas las got�culas y suma su masa (que es 1 por definici�n) 
     * ponderada seg�n su distancia al punto (x,y) usando el Kernel.
     * Las got�culas m�s cercanas influiran m�s que las lejanas, y las que
     * est�n a una distancia mayor que 1 no influiran.
     * @param x Posici�n x del punto
     * @param y Posici�n y del punto
     * @return  La densidad alrededor de ese punto
     */    
    protected float CalcDensidad(float x, float y) {
        float densidad = 0;
        for(Goticula g: gotas) { 
            // La suma ya se cuenta en CalcDensidad
            densidad += CalcDensidadIter(x, y, g);
        }
        return densidad;
    }
   
    /**
     * Calcula la fuerza debida a diferenciales de presi�n.
     * Atenci�n: La densidad se calcula con las posiciones actualizadas y
     * la presi�n con la posici�n original de las part�culas (para que la 
     * simulaci�n sea m�s estable)
     * @param gi La got�cula sobre la que se calcula la fuerza
     * @return La fuerza que se realiza sobre la got�cula. Se devuelve
     * en un vector (x,y) (clase VecXY).
     */    
    protected VecXY CalcPresion(Goticula gi) {
        VecXY f = new VecXY(0,0);
        // Se acumula el efecto del resto de got�culas
        for(Goticula gj: gotas) {
            f.Add(CalcPresionIter(gi, gj));
        }       
        f.Scale(1/gi.d);
        return f;
    }
   
    /**
     * Calcula la fuerza debida a la viscosidad entre got�culas.
     * @param gi La got�cula sobre la que se calcula la fuerza
     * @return La fuerza (fx,fy) que se realiza sobre la got�cula. Se devuelve
     * en un vector (x,y) (clase VecXY).
     */        
    protected VecXY CalcViscosidad(Goticula gi) {
        VecXY f = new VecXY(0,0);
        for(Goticula gj: gotas) {
            f.Add(CalcViscosidadIter(gi, gj));            
        }
        return f;
    }

    /**
     * M�todo que se debe sobreescribir para adaptar vuestras estructuras de
     * datos auxiliares (ED) al cambio de posici�n de las got�culas.
     * Nota: Si no necesitais adaptar vuestras ED o bien lo realizais
     * sobreescribiendo el m�todo SetPos en una clase que hereda de Got�cula
     * entonces no sobreescribais el m�todo (que no hace nada), por supuesto.
     */    
    protected void ReestructuraED() {
        
    }    
    
}
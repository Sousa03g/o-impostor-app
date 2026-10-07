package com.exemplo.oimpostor

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.math.roundToInt
import kotlin.random.Random

// ==========================================
// PALETA DE CORES & TEMA (Clean & Minimalist)
// ==========================================
private val CleanBackground = Color(0xFFF7F8FA)
private val CleanSurface = Color(0xFFFFFFFF)
private val CleanSurfaceVariant = Color(0xFFEFF2F5)
private val CleanTextPrimary = Color(0xFF1E232A)
private val CleanTextSecondary = Color(0xFF6B7280)
private val CleanAccent = Color(0xFF334155) // Slate elegante
private val CleanBorder = Color(0xFFE2E8F0)
private val CleanImpostorAlert = Color(0xFFDC2626) // Vermelho sutil e refinado
private val CleanHintHighlight = Color(0xFFD97706) // Âmbar sutil para dicas

// Modos de Dica
enum class HintMode(val label: String, val description: String) {
    ONE_WORD("Palavra Única", "Dica difícil: apenas 1 única palavra relacionada indiretamente."),
    NONE("Sem Dica", "Purista: o impostor não recebe nenhuma palavra e deve descobrir só ouvindo.")
}

// ==========================================
// MODELOS DE DADOS E PALAVRAS COM DICA DE 1 PALAVRA (PT-BR)
// ==========================================
data class SecretItem(
    val word: String,
    val singleWordHint: String
)

data class ThemeCategory(
    val id: String,
    val title: String,
    val items: List<SecretItem>
)

object WordRepository {
    val defaultCategories = listOf(
        ThemeCategory(
            id = "comidas_e_bebidas",
            title = "Comidas & Bebidas",
            items = listOf(
                SecretItem("Pizza", "Fatia"),
                SecretItem("Hambúrguer", "Chapa"),
                SecretItem("Sushi", "Alga"),
                SecretItem("Lasanha", "Camadas"),
                SecretItem("Feijoada", "Panela"),
                SecretItem("Churrasco", "Brasa"),
                SecretItem("Coxinha", "Fritura"),
                SecretItem("Pastel", "Feira"),
                SecretItem("Pão de Queijo", "Polvilho"),
                SecretItem("Açaí", "Tigela"),
                SecretItem("Brigadeiro", "Granulado"),
                SecretItem("Tacos", "Tortilha"),
                SecretItem("Sorvete", "Casquinha"),
                SecretItem("Chocolate", "Cacau"),
                SecretItem("Café Expresso", "Grão"),
                SecretItem("Suco de Laranja", "Bagaço"),
                SecretItem("Panqueca", "Disco"),
                SecretItem("Fondue", "Espeto"),
                SecretItem("Strogonoff", "Creme"),
                SecretItem("Guaraná", "Amazônia"),
                SecretItem("Pipoca", "Cinema"),
                SecretItem("Cerveja", "Cevada"),
                SecretItem("Vinho", "Barril"),
                SecretItem("Tapioca", "Goma"),
                SecretItem("Pudim", "Calda"),
                SecretItem("Churros", "Doce"),
                SecretItem("Pão Francês", "Casca"),
                SecretItem("Miojo", "Minutos"),
                SecretItem("Chimarrão", "Bomba"),
                SecretItem("Caipirinha", "Pilão"),
                SecretItem("Donut", "Furo"),
                SecretItem("Moqueca", "Barro"),
                SecretItem("Cuscuz", "Vapor"),
                SecretItem("Croissant", "Manteiga"),
                SecretItem("Pastel de Belém", "Canela"),
                SecretItem("Risoto", "Arbóreo"),
                SecretItem("Guacamole", "Abacate"),
                SecretItem("Waffle", "Quadriculado"),
                SecretItem("Algodão-Doce", "Nuvem"),
                SecretItem("Chá de Camomila", "Calmante"),
                SecretItem("Coxinha de Frango", "Catupiry"),
                SecretItem("Esfirra", "Triângulo"),
                SecretItem("Panetone", "Frutas"),
                SecretItem("Pipoca Doce", "Caramelo"),
                SecretItem("Batata Frita", "Ketchup"),
                SecretItem("Fondue de Queijo", "Panela"),
                SecretItem("Brownie", "Nozes"),
                SecretItem("Churrasco Gaúcho", "Espeto"),
                SecretItem("Sopa de Capeletti", "Caldo"),
                SecretItem("Palha Italiana", "Biscoito")
            )
        ),
        ThemeCategory(
            id = "filmes_e_series",
            title = "Filmes & Séries",
            items = listOf(
                SecretItem("Titanic", "Iceberg"),
                SecretItem("Avatar", "Pandora"),
                SecretItem("O Poderoso Chefão", "Família"),
                SecretItem("Harry Potter", "Varinha"),
                SecretItem("Senhor dos Anéis", "Anel"),
                SecretItem("Vingadores", "Joias"),
                SecretItem("Matrix", "Pílula"),
                SecretItem("Interestelar", "Gravidade"),
                SecretItem("Stranger Things", "Portal"),
                SecretItem("Breaking Bad", "Química"),
                SecretItem("Game of Thrones", "Trono"),
                SecretItem("Round 6", "Batatinha"),
                SecretItem("Star Wars", "Sabre"),
                SecretItem("Jurassic Park", "Âmbar"),
                SecretItem("Shrek", "Pântano"),
                SecretItem("Coringa", "Riso"),
                SecretItem("Toy Story", "Baú"),
                SecretItem("Gladiador", "Arena"),
                SecretItem("Homem-Aranha", "Teia"),
                SecretItem("O Rei Leão", "Pedra"),
                SecretItem("La Casa de Papel", "Máscara"),
                SecretItem("Piratas do Caribe", "Bússola"),
                SecretItem("De Volta para o Futuro", "Raio"),
                SecretItem("V de Vingança", "Pólvora"),
                SecretItem("Peaky Blinders", "Boina"),
                SecretItem("Clube da Luta", "Sabão"),
                SecretItem("Psicose", "Cortina"),
                SecretItem("O Iluminado", "Machado"),
                SecretItem("Pulp Fiction", "Maleta"),
                SecretItem("Pantera Negra", "Vibranium"),
                SecretItem("O Exterminador do Futuro", "Metal Líquido"),
                SecretItem("Mad Max", "Areia"),
                SecretItem("Indiana Jones", "Chicote"),
                SecretItem("Cisne Negro", "Ponta"),
                SecretItem("Rocky", "Degraus"),
                SecretItem("E.T. O Extraterrestre", "Cesto")
            )
        ),
        ThemeCategory(
            id = "lugares_do_mundo",
            title = "Lugares do Mundo",
            items = listOf(
                SecretItem("Paris", "Ferro"),
                SecretItem("Tóquio", "Neon"),
                SecretItem("Nova York", "Ilha"),
                SecretItem("Rio de Janeiro", "Corcovado"),
                SecretItem("Roma", "Império"),
                SecretItem("Londres", "Névoa"),
                SecretItem("Cairo", "Areia"),
                SecretItem("Sydney", "Conchas"),
                SecretItem("Veneza", "Gôndola"),
                SecretItem("Machu Picchu", "Pedra"),
                SecretItem("Dubai", "Oásis"),
                SecretItem("Muralha da China", "Fronteira"),
                SecretItem("Havaí", "Onda"),
                SecretItem("Amsterdã", "Canal"),
                SecretItem("Barcelona", "Mosaico"),
                SecretItem("Polo Norte", "Bússola"),
                SecretItem("Deserto do Saara", "Duna"),
                SecretItem("Fernando de Noronha", "Golfinho"),
                SecretItem("Las Vegas", "Dado"),
                SecretItem("Atenas", "Coluna"),
                SecretItem("Gramado", "Hortênsia"),
                SecretItem("Maracanã", "Gramado"),
                SecretItem("Floresta Amazônica", "Copa"),
                SecretItem("Pão de Açúcar", "Bonde"),
                SecretItem("Torre Eiffel", "Ferro"),
                SecretItem("Cristo Redentor", "Braços"),
                SecretItem("Coliseu", "Arena"),
                SecretItem("Estátua da Liberdade", "Tocha"),
                SecretItem("Taj Mahal", "Mármore"),
                SecretItem("Big Ben", "Badalo"),
                SecretItem("Grand Canyon", "Erosão"),
                SecretItem("Torre de Pisa", "Inclinação"),
                SecretItem("Cataratas do Iguaçu", "Queda"),
                SecretItem("Monte Fuji", "Vulcão"),
                SecretItem("Acrópole", "Coluna"),
                SecretItem("Hollywood", "Letreiro"),
                SecretItem("Ilha de Páscoa", "Moai"),
                SecretItem("Disney World", "Castelo"),
                SecretItem("Monte Everest", "Cume"),
                SecretItem("Stonehenge", "Monólito"),
                SecretItem("Canal do Panamá", "Eclusa"),
                SecretItem("Muralha de Berlim", "Grafite"),
                SecretItem("Vaticano", "Fumaça"),
                SecretItem("Galápagos", "Tartaruga")
            )
        ),
        ThemeCategory(
            id = "animais",
            title = "Animais",
            items = listOf(
                SecretItem("Leão", "Juba"),
                SecretItem("Elefante", "Marfim"),
                SecretItem("Pinguim", "Gelo"),
                SecretItem("Girafa", "Altura"),
                SecretItem("Tubarão", "Barbatana"),
                SecretItem("Canguru", "Bolsa"),
                SecretItem("Golfinho", "Salto"),
                SecretItem("Águia", "Garra"),
                SecretItem("Panda", "Bambu"),
                SecretItem("Lobo", "Alcateia"),
                SecretItem("Camaleão", "Disfarce"),
                SecretItem("Preguiça", "Galho"),
                SecretItem("Crocodilo", "Pântano"),
                SecretItem("Urso Polar", "Ártico"),
                SecretItem("Tartaruga", "Casco"),
                SecretItem("Gato", "Telhado"),
                SecretItem("Cachorro", "Coleira"),
                SecretItem("Coruja", "Noite"),
                SecretItem("Zebra", "Listra"),
                SecretItem("Macaco", "Cauda"),
                SecretItem("Morcego", "Eco"),
                SecretItem("Cobra", "Veneno"),
                SecretItem("Abelha", "Colmeia"),
                SecretItem("Formiga", "Trilha"),
                SecretItem("Cavalo", "Ferradura"),
                SecretItem("Ornitorrinco", "Bico"),
                SecretItem("Bicho-Preguiça", "Galho"),
                SecretItem("Polvo", "Tinta"),
                SecretItem("Pavão", "Leque"),
                SecretItem("Pica-Pau", "Tronco"),
                SecretItem("Hiena", "Gargalhada"),
                SecretItem("Avestruz", "Areia"),
                SecretItem("Vagalume", "Lanterna"),
                SecretItem("Castor", "Represa"),
                SecretItem("Foca", "Equilíbrio"),
                SecretItem("Caranguejo", "Lado"),
                SecretItem("Esquilo", "Noz"),
                SecretItem("Camelo", "Reserva"),
                SecretItem("Tamanduá", "Formigueiro"),
                SecretItem("Baleia Azul", "Esguicho"),
                SecretItem("Rinoceronte", "Chifre"),
                SecretItem("Sapo", "Pulo"),
                SecretItem("Gavião", "Garra"),
                SecretItem("Tucano", "Cores"),
                SecretItem("Hipopótamo", "Lama")
            )
        ),
        ThemeCategory(
            id = "objetos_do_dia_a_dia",
            title = "Objetos do Dia a Dia",
            items = listOf(
                SecretItem("Celular", "Tela"),
                SecretItem("Notebook", "Teclado"),
                SecretItem("Relógio", "Pulso"),
                SecretItem("Óculos de Grau", "Lente"),
                SecretItem("Garrafa Térmica", "Vácuo"),
                SecretItem("Guarda-Chuva", "Garoa"),
                SecretItem("Mochila", "Zíper"),
                SecretItem("Chaveiro", "Fechadura"),
                SecretItem("Fones de Ouvido", "Volume"),
                SecretItem("Escova de Dentes", "Espuma"),
                SecretItem("Cafeteira", "Filtro"),
                SecretItem("Carteira", "Bolso"),
                SecretItem("Caneta", "Tinta"),
                SecretItem("Travesseiro", "Fronha"),
                SecretItem("Espelho", "Reflexo"),
                SecretItem("Carregador", "Tomada"),
                SecretItem("Tesoura", "Lâmina"),
                SecretItem("Cadeira", "Encosto"),
                SecretItem("Geladeira", "Motor"),
                SecretItem("Micro-ondas", "Prato"),
                SecretItem("Toalha", "Fibra"),
                SecretItem("Chave", "Segredo"),
                SecretItem("Lâmpada", "Filamento"),
                SecretItem("Ventilador", "Hélice"),
                SecretItem("Grampeador", "Mola"),
                SecretItem("Isqueiro", "Faísca"),
                SecretItem("Vassoura", "Cerdas"),
                SecretItem("Despertador", "Soneca"),
                SecretItem("Zíper", "Trilho"),
                SecretItem("Balança", "Massa"),
                SecretItem("Lanterna", "Pilha"),
                SecretItem("Tesoura sem Ponta", "Escola"),
                SecretItem("Pregador de Roupa", "Varal"),
                SecretItem("Ralador", "Furos"),
                SecretItem("Abajur", "Cúpula"),
                SecretItem("Funil", "Gargalo"),
                SecretItem("Balde", "Alça"),
                SecretItem("Cinzeiro", "Bituca"),
                SecretItem("Espátula", "Borda"),
                SecretItem("Guarda-Sol", "Sombra"),
                SecretItem("Saca-Rolhas", "Espiral"),
                SecretItem("Tábua de Passar", "Acolchoado"),
                SecretItem("Extensão Elétrica", "Metros"),
                SecretItem("Regador", "Crivo")
            )
        ),
        ThemeCategory(
            id = "profissoes",
            title = "Profissões",
            items = listOf(
                SecretItem("Médico", "Jaleco"),
                SecretItem("Astronauta", "Órbita"),
                SecretItem("Professor", "Quadro"),
                SecretItem("Advogado", "Tribunal"),
                SecretItem("Piloto de Avião", "Cabine"),
                SecretItem("Chef de Cozinha", "Menu"),
                SecretItem("Policial", "Distintivo"),
                SecretItem("Bombeiro", "Sirene"),
                SecretItem("Detetive", "Lupa"),
                SecretItem("Arquiteto", "Planta"),
                SecretItem("Programador", "Código"),
                SecretItem("Jornalista", "Pauta"),
                SecretItem("Veterinário", "Clínica"),
                SecretItem("Fotógrafo", "Flash"),
                SecretItem("Cientista", "Tubo"),
                SecretItem("Psicólogo", "Divã"),
                SecretItem("Juiz", "Martelo"),
                SecretItem("Músico", "Partitura"),
                SecretItem("Dentista", "Broca"),
                SecretItem("Eletricista", "Fio"),
                SecretItem("Marceneiro", "Serragem"),
                SecretItem("Pintor", "Cavalete"),
                SecretItem("Ator", "Roteiro"),
                SecretItem("Garçom", "Bandeja"),
                SecretItem("Padeiro", "Fermento"),
                SecretItem("Costureiro", "Dedal"),
                SecretItem("Barbeiro", "Navalha"),
                SecretItem("Maestro", "Batuta"),
                SecretItem("Coveiro", "Pá"),
                SecretItem("Biólogo", "Lupa"),
                SecretItem("Meteorologista", "Nuvem"),
                SecretItem("Agricultor", "Safra"),
                SecretItem("Bibliotecário", "Silêncio"),
                SecretItem("Encanador", "Vazamento"),
                SecretItem("Mecânico", "Graxa"),
                SecretItem("Guarda-Vidas", "Apito"),
                SecretItem("Pescador", "Rede"),
                SecretItem("Fotógrafo Noturno", "Tripé"),
                SecretItem("Guia Turístico", "Microfone"),
                SecretItem("Jardineiro", "Poda"),
                SecretItem("Garimpeiro", "Bateia"),
                SecretItem("Paleontólogo", "Fóssil"),
                SecretItem("Dublador", "Voz"),
                SecretItem("Juiz de Linha", "Bandeirinha")
            )
        ),
        ThemeCategory(
            id = "lugares_e_ambientes",
            title = "Lugares & Ambientes",
            items = listOf(
                SecretItem("Hospital", "Maca"),
                SecretItem("Aeroporto", "Pista"),
                SecretItem("Biblioteca", "Silêncio"),
                SecretItem("Academia", "Anilha"),
                SecretItem("Museu", "Guia"),
                SecretItem("Cinema", "Projetor"),
                SecretItem("Supermercado", "Carrinho"),
                SecretItem("Parque de Diversões", "Fila"),
                SecretItem("Praia", "Maré"),
                SecretItem("Banco", "Cofre"),
                SecretItem("Estádio", "Arquibancada"),
                SecretItem("Hotel", "Chaveiro"),
                SecretItem("Teatro", "Cortina"),
                SecretItem("Padaria", "Fornada"),
                SecretItem("Igreja", "Sino"),
                SecretItem("Zoológico", "Grade"),
                SecretItem("Farmácia", "Receita"),
                SecretItem("Escola", "Sinal"),
                SecretItem("Cemitério", "Lápide"),
                SecretItem("Estação de Trem", "Trilho"),
                SecretItem("Circo", "Lona"),
                SecretItem("Posto de Combustível", "Bomba"),
                SecretItem("Fazenda", "Cerca"),
                SecretItem("Laboratório", "Tubo"),
                SecretItem("Estádio de Futebol", "Arquibancada"),
                SecretItem("Sapataria", "Cola"),
                SecretItem("Floricultura", "Pétalas"),
                SecretItem("Açougue", "Gancho"),
                SecretItem("Lavanderia", "Cesto"),
                SecretItem("Observatório", "Cúpula"),
                SecretItem("Estacionamento", "Vaga"),
                SecretItem("Prisão", "Grade"),
                SecretItem("Aquário", "Vidro"),
                SecretItem("Oficina Mecânica", "Elevador"),
                SecretItem("Porto", "Guindaste"),
                SecretItem("Padaria Artesanal", "Fornada"),
                SecretItem("Salão de Beleza", "Secador"),
                SecretItem("Rodoviária", "Plataforma"),
                SecretItem("Tribunal", "Bancada"),
                SecretItem("Feira Livre", "Barraca")
            )
        ),
        ThemeCategory(
            id = "hobbies_e_esportes",
            title = "Hobbies & Esportes",
            items = listOf(
                SecretItem("Futebol", "Trave"),
                SecretItem("Basquete", "Aro"),
                SecretItem("Vôlei", "Manchete"),
                SecretItem("Tênis", "Saque"),
                SecretItem("Natação", "Touca"),
                SecretItem("Ciclismo", "Pedal"),
                SecretItem("Skate", "Lixa"),
                SecretItem("Surfe", "Quilha"),
                SecretItem("Xadrez", "Xeque"),
                SecretItem("Videogame", "Controle"),
                SecretItem("Pesca", "Isca"),
                SecretItem("Karaokê", "Microfone"),
                SecretItem("Acampamento", "Fogueira"),
                SecretItem("Boliche", "Canaleta"),
                SecretItem("Corrida", "Largada"),
                SecretItem("Dança", "Compasso"),
                SecretItem("Pintura", "Paleta"),
                SecretItem("Jardinagem", "Adubo"),
                SecretItem("Yoga", "Tapete"),
                SecretItem("Escalada", "Mosquetão"),
                SecretItem("Tênis de Mesa", "Redinha"),
                SecretItem("Arco e Flecha", "Alvo"),
                SecretItem("Sinuca", "Giz"),
                SecretItem("Boxe", "Corda"),
                SecretItem("Trompete", "Pistão"),
                SecretItem("Origami", "Dobra"),
                SecretItem("Tricô", "Agulhas"),
                SecretItem("Mágica", "Baralho"),
                SecretItem("Judô", "Faixa"),
                SecretItem("Golfe", "Buraco"),
                SecretItem("Remo", "Pá"),
                SecretItem("Dança de Salão", "Passo"),
                SecretItem("Marcenaria Amadora", "Tupia"),
                SecretItem("Astronomia Amadora", "Telescópio"),
                SecretItem("Cozinha Gastronômica", "Azeite"),
                SecretItem("Boliche de Grama", "Bocha"),
                SecretItem("Voo Livre", "Vento"),
                SecretItem("Karatê", "Kiai"),
                SecretItem("Handebol", "Resina"),
                SecretItem("Polo Aquático", "Touca"),
                SecretItem("Rugby", "Oval"),
                SecretItem("Fórmula 1", "Pitstop"),
                SecretItem("Atletismo", "Bastão")
            )
        ),
        ThemeCategory(
            id = "tecnologia_e_geek",
            title = "Tecnologia & Geek",
            items = listOf(
                SecretItem("Wi-Fi", "Ondas"),
                SecretItem("Teclado", "Mecânico"),
                SecretItem("Mousepad", "Deslize"),
                SecretItem("Processador", "Silício"),
                SecretItem("Placa de Vídeo", "Ventoinha"),
                SecretItem("Pen Drive", "Porta"),
                SecretItem("Satélite", "Órbita"),
                SecretItem("Robô", "Articulação"),
                SecretItem("Servidor", "Rack"),
                SecretItem("Câmera de Segurança", "Ângulo")
            )
        ),
        ThemeCategory(
            id = "natureza_e_fenomenos",
            title = "Natureza & Fenômenos",
            items = listOf(
                SecretItem("Terremoto", "Fissura"),
                SecretItem("Tornado", "Funil"),
                SecretItem("Aurora Boreal", "Magnetismo"),
                SecretItem("Vulcão Ativo", "Magma"),
                SecretItem("Nevasca", "Visibilidade"),
                SecretItem("Tsunami", "Recuo"),
                SecretItem("Eclipse Solar", "Coroa"),
                SecretItem("Geada", "Cristal"),
                SecretItem("Arco-Íris", "Refração"),
                SecretItem("Relâmpago", "Trovão")
            )
        ),
        ThemeCategory(
            id = "mitologia_e_ficcao",
            title = "Mitologia & Ficção",
            items = listOf(
                SecretItem("Dragão", "Escama"),
                SecretItem("Vampiro", "Espelho"),
                SecretItem("Lobisomem", "Cheia"),
                SecretItem("Sereia", "Canto"),
                SecretItem("Múmia", "Faixas"),
                SecretItem("Fênix", "Cinzas"),
                SecretItem("Centauro", "Casco"),
                SecretItem("Zumbi", "Lentidão"),
                SecretItem("Fantasma", "Transparência"),
                SecretItem("Ciclope", "Único")
            )
        ),
    )

    private const val PREFS_NAME = "impostor_custom_words"
    private const val KEY_CUSTOM_ITEMS = "custom_words_list"

    /**
     * Carrega as palavras personalizadas cadastradas pelo usuário a partir do SharedPreferences.
     * Decodifica a string serializada no formato 'palavra|||dica;;;palavra2|||dica2'.
     *
     * @param context Contexto do Android para acesso aos arquivos de preferências.
     * @return Categoria ThemeCategory contendo as palavras customizadas pelo usuário.
     */
    fun loadCustomCategory(context: Context): ThemeCategory {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_CUSTOM_ITEMS, "") ?: ""
        val list = mutableListOf<SecretItem>()
        if (raw.isNotBlank()) {
            raw.split(";;;").forEach { entry ->
                val parts = entry.split("|||")
                if (parts.isNotEmpty()) {
                    val word = parts[0].trim()
                    val hint = if (parts.size > 1 && parts[1].isNotBlank()) parts[1].trim() else "Pista"
                    if (word.isNotBlank()) {
                        list.add(SecretItem(word, hint))
                    }
                }
            }
        }
        return ThemeCategory(
            id = "personalizadas",
            title = "Minhas Palavras (${list.size})",
            items = list
        )
    }

    /**
     * Adiciona e persiste uma nova palavra personalizada no dispositivo.
     * Higieniza os dados e garante que a dica seja restrita a apenas 1 palavra.
     *
     * @param context Contexto do Android para salvar nos SharedPreferences.
     * @param word Palavra secreta digitada pelo usuário.
     * @param singleWordHint Pista enigmática de 1 palavra associada ao termo.
     */
    fun addCustomWord(context: Context, word: String, singleWordHint: String) {
        val current = loadCustomCategory(context).items.toMutableList()
        val sHint = if (singleWordHint.isBlank()) "Pista" else singleWordHint.trim().split(" ").first()
        current.removeAll { it.word.equals(word.trim(), ignoreCase = true) }
        current.add(0, SecretItem(word.trim(), sHint))

        val serialized = current.joinToString(";;;") { "${it.word}|||${it.singleWordHint}" }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CUSTOM_ITEMS, serialized).apply()
    }
}

// ==========================================
// ESTADOS DO JOGO
// ==========================================
enum class GameScreen {
    SETUP,
    PASS_PHONE,
    DISCUSSION,
    REVEAL
}

data class PlayerRole(
    val playerIndex: Int,
    val playerName: String,
    val isImpostor: Boolean
)

data class GameState(
    val screen: GameScreen = GameScreen.SETUP,
    val totalPlayers: Int = 4,
    val totalImpostors: Int = 1,
    val hintMode: HintMode = HintMode.ONE_WORD,
    val playerNames: List<String> = listOf("Jogador 1", "Jogador 2", "Jogador 3", "Jogador 4"),
    val selectedCategoryIds: Set<String> = setOf("comidas"),
    val secretWord: String = "",
    val secretHint: String = "",
    val chosenCategoryTitle: String = "",
    val players: List<PlayerRole> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val startingPlayerName: String = "Jogador 1"
)

// ==========================================
// ACTIVITY PRINCIPAL
// ==========================================
class MainActivity : ComponentActivity() {
    /**
     * Ponto de entrada do ciclo de vida da Activity Android.
     * Inicializa a interface Compose através da função de extensão setContent.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImpostorApp()
        }
    }
}

// ==========================================
// COMPOSABLE RAIZ & FLUXO
// ==========================================
/**
 * Composable raiz da aplicação ("Single Source of Truth").
 *
 * Responsável por:
 * 1. Manter o GameState em memória durante a sessão.
 * 2. Carregar categorias padrão e palavras salvas localmente no dispositivo.
 * 3. Gerenciar as transições animadas entre telas (Crossfade):
 *    - SETUP: Configuração da partida
 *    - PASS_PHONE: Distribuição secreta de papéis
 *    - DISCUSSION: Fase de debate e perguntas
 *    - REVEAL: Apuração e revelação final dos impostores
 */
@Composable
fun ImpostorApp() {
    val context = LocalContext.current
    var customCategory by remember { mutableStateOf(WordRepository.loadCustomCategory(context)) }
    val allCategories = remember(customCategory) {
        if (customCategory.items.isNotEmpty()) {
            WordRepository.defaultCategories + customCategory
        } else {
            WordRepository.defaultCategories
        }
    }

    var gameState by remember { mutableStateOf(GameState()) }

    fun refreshCustomCategory() {
        customCategory = WordRepository.loadCustomCategory(context)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CleanBackground
    ) {
        Crossfade(targetState = gameState.screen, label = "ScreenTransition") { screen ->
            when (screen) {
                GameScreen.SETUP -> SetupScreen(
                    gameState = gameState,
                    availableCategories = allCategories,
                    customItemCount = customCategory.items.size,
                    onUpdatePlayers = { newCount ->
                        val maxImpostors = (newCount - 1).coerceAtLeast(1)
                        val updatedNames = (1..newCount).map { i ->
                            gameState.playerNames.getOrNull(i - 1) ?: "Jogador $i"
                        }
                        gameState = gameState.copy(
                            totalPlayers = newCount,
                            playerNames = updatedNames,
                            totalImpostors = gameState.totalImpostors.coerceAtMost(maxImpostors)
                        )
                    },
                    onUpdatePlayerName = { index, newName ->
                        val updated = gameState.playerNames.toMutableList()
                        if (index in updated.indices) {
                            updated[index] = newName
                            gameState = gameState.copy(playerNames = updated)
                        }
                    },
                    onUpdateImpostors = { newImpostors ->
                        gameState = gameState.copy(totalImpostors = newImpostors)
                    },
                    onUpdateHintMode = { newMode ->
                        gameState = gameState.copy(hintMode = newMode)
                    },
                    onToggleCategory = { categoryId ->
                        val current = gameState.selectedCategoryIds.toMutableSet()
                        if (current.contains(categoryId)) {
                            if (current.size > 1) {
                                current.remove(categoryId)
                            }
                        } else {
                            current.add(categoryId)
                        }
                        gameState = gameState.copy(selectedCategoryIds = current)
                    },
                    onAddWord = { word, hint ->
                        WordRepository.addCustomWord(context, word, hint)
                        refreshCustomCategory()
                    },
                    onStartGame = {
                        val activeCategories = allCategories.filter { it.id in gameState.selectedCategoryIds }
                        val pool = activeCategories.flatMap { it.items }.ifEmpty {
                            WordRepository.defaultCategories.first().items
                        }
                        val chosen = pool.random()

                        val parentCategory = activeCategories.find { it.items.any { item -> item.word == chosen.word } }
                        val categoryTitle = if (activeCategories.size > 1) {
                            "${parentCategory?.title ?: "Misto"} (Multi-Tema)"
                        } else {
                            parentCategory?.title ?: "Geral"
                        }

                        val impostorIndices = (0 until gameState.totalPlayers)
                            .shuffled()
                            .take(gameState.totalImpostors)
                            .toSet()

                        val playerRoles = (0 until gameState.totalPlayers).map { idx ->
                            PlayerRole(
                                playerIndex = idx,
                                playerName = gameState.playerNames.getOrElse(idx) { "Jogador ${idx + 1}" }.ifBlank { "Jogador ${idx + 1}" },
                                isImpostor = idx in impostorIndices
                            )
                        }

                        val starter = playerRoles.random().playerName
                        val assignedHint = if (gameState.hintMode == HintMode.ONE_WORD) chosen.singleWordHint else ""

                        gameState = gameState.copy(
                            screen = GameScreen.PASS_PHONE,
                            secretWord = chosen.word,
                            secretHint = assignedHint,
                            chosenCategoryTitle = categoryTitle,
                            players = playerRoles,
                            currentPlayerIndex = 0,
                            startingPlayerName = starter
                        )
                    }
                )

                GameScreen.PASS_PHONE -> PassPhoneScreen(
                    player = gameState.players[gameState.currentPlayerIndex],
                    category = gameState.chosenCategoryTitle,
                    secretWord = gameState.secretWord,
                    secretHint = gameState.secretHint,
                    hintMode = gameState.hintMode,
                    isLastPlayer = gameState.currentPlayerIndex == gameState.players.size - 1,
                    nextPlayerName = if (gameState.currentPlayerIndex < gameState.players.size - 1) {
                        gameState.players[gameState.currentPlayerIndex + 1].playerName
                    } else null,
                    onNextPlayerConfirmed = {
                        if (gameState.currentPlayerIndex < gameState.players.size - 1) {
                            gameState = gameState.copy(currentPlayerIndex = gameState.currentPlayerIndex + 1)
                        } else {
                            gameState = gameState.copy(screen = GameScreen.DISCUSSION)
                        }
                    }
                )

                GameScreen.DISCUSSION -> DiscussionScreen(
                    startingPlayer = gameState.startingPlayerName,
                    onFinishDiscussion = {
                        gameState = gameState.copy(screen = GameScreen.REVEAL)
                    }
                )

                GameScreen.REVEAL -> RevealScreen(
                    secretWord = gameState.secretWord,
                    secretHint = gameState.secretHint,
                    hintMode = gameState.hintMode,
                    category = gameState.chosenCategoryTitle,
                    impostors = gameState.players.filter { it.isImpostor }.map { it.playerName },
                    onPlayAgain = {
                        val activeCategories = allCategories.filter { it.id in gameState.selectedCategoryIds }
                        val pool = activeCategories.flatMap { it.items }.ifEmpty {
                            WordRepository.defaultCategories.first().items
                        }
                        val chosen = pool.random()
                        val parentCategory = activeCategories.find { it.items.any { item -> item.word == chosen.word } }
                        val categoryTitle = if (activeCategories.size > 1) {
                            "${parentCategory?.title ?: "Misto"} (Multi-Tema)"
                        } else {
                            parentCategory?.title ?: "Geral"
                        }

                        val impostorIndices = (0 until gameState.totalPlayers)
                            .shuffled()
                            .take(gameState.totalImpostors)
                            .toSet()

                        val playerRoles = (0 until gameState.totalPlayers).map { idx ->
                            PlayerRole(
                                playerIndex = idx,
                                playerName = gameState.playerNames.getOrElse(idx) { "Jogador ${idx + 1}" }.ifBlank { "Jogador ${idx + 1}" },
                                isImpostor = idx in impostorIndices
                            )
                        }

                        val starter = playerRoles.random().playerName
                        val assignedHint = if (gameState.hintMode == HintMode.ONE_WORD) chosen.singleWordHint else ""

                        gameState = gameState.copy(
                            screen = GameScreen.PASS_PHONE,
                            secretWord = chosen.word,
                            secretHint = assignedHint,
                            chosenCategoryTitle = categoryTitle,
                            players = playerRoles,
                            currentPlayerIndex = 0,
                            startingPlayerName = starter
                        )
                    },
                    onBackToSetup = {
                        gameState = gameState.copy(
                            screen = GameScreen.SETUP,
                            currentPlayerIndex = 0
                        )
                    }
                )
            }
        }
    }
}

// ==========================================
// TELA 1: CONFIGURAÇÃO DA PARTIDA
// ==========================================
/**
 * Tela 1: Configuração da Partida (SetupScreen).
 *
 * Permite ao anfitrião:
 * - Ajustar a quantidade de jogadores (mínimo 3).
 * - Definir a quantidade de impostores (com limite validado).
 * - Abrir o diálogo para personalizar o nome de cada jogador.
 * - Escolher o modo de dica (Palavra Única ou Sem Dica).
 * - Selecionar um ou múltiplos temas simultaneamente (Multi-Theme).
 * - Cadastrar palavras e dicas personalizadas em PT-BR.
 *
 * @param gameState Estado atual da partida.
 * @param availableCategories Lista de categorias disponíveis (padrão + personalizadas).
 * @param customItemCount Quantidade de palavras salvas pelo usuário.
 * @param onUpdatePlayers Callback disparado ao alterar o total de jogadores.
 * @param onUpdatePlayerName Callback disparado ao editar o nome de um jogador.
 * @param onUpdateImpostors Callback disparado ao alterar a quantidade de impostores.
 * @param onUpdateHintMode Callback disparado ao alternar o modo de dica.
 * @param onToggleCategory Callback disparado ao marcar/desmarcar uma categoria.
 * @param onAddWord Callback para registrar uma nova palavra customizada.
 * @param onStartGame Callback para sortear papéis e iniciar o jogo.
 */
@Composable
fun SetupScreen(
    gameState: GameState,
    availableCategories: List<ThemeCategory>,
    customItemCount: Int,
    onUpdatePlayers: (Int) -> Unit,
    onUpdatePlayerName: (Int, String) -> Unit,
    onUpdateImpostors: (Int) -> Unit,
    onUpdateHintMode: (HintMode) -> Unit,
    onToggleCategory: (String) -> Unit,
    onAddWord: (String, String) -> Unit,
    onStartGame: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showEditNamesDialog by remember { mutableStateOf(false) }
    var showAddWordDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Cabeçalho
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        ) {
            Text(
                text = "O IMPOSTOR",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = CleanTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Dedução Social & Blefe",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = CleanTextSecondary
            )
        }

        // Card: Jogadores + Edição de Nomes
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CleanSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CleanBorder))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Jogadores",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CleanTextPrimary
                        )
                        Text(
                            text = "Mínimo 3 participantes",
                            fontSize = 13.sp,
                            color = CleanTextSecondary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = { if (gameState.totalPlayers > 3) onUpdatePlayers(gameState.totalPlayers - 1) },
                            enabled = gameState.totalPlayers > 3,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (gameState.totalPlayers > 3) CleanSurfaceVariant else Color.Transparent)
                        ) {
                            Text(
                                text = "−",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (gameState.totalPlayers > 3) CleanTextPrimary else CleanTextSecondary.copy(alpha = 0.4f)
                            )
                        }

                        Text(
                            text = gameState.totalPlayers.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = CleanTextPrimary,
                            modifier = Modifier.widthIn(min = 20.dp),
                            textAlign = TextAlign.Center
                        )

                        IconButton(
                            onClick = { if (gameState.totalPlayers < 20) onUpdatePlayers(gameState.totalPlayers + 1) },
                            enabled = gameState.totalPlayers < 20,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (gameState.totalPlayers < 20) CleanSurfaceVariant else Color.Transparent)
                        ) {
                            Text(
                                text = "+",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (gameState.totalPlayers < 20) CleanTextPrimary else CleanTextSecondary.copy(alpha = 0.4f)
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = { showEditNamesDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CleanTextPrimary),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(CleanBorder)
                    )
                ) {
                    Text(
                        text = "Editar Nomes dos Jogadores",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Card: Impostores
        val maxImpostors = (gameState.totalPlayers - 1).coerceAtLeast(1)
        CleanCounterCard(
            title = "Impostores",
            subtitle = "Máximo de $maxImpostors para esta rodada",
            value = gameState.totalImpostors,
            minValue = 1,
            maxValue = maxImpostors,
            onValueChange = onUpdateImpostors
        )

        // Card: Dica para o Impostor (Palavra Única / Sem Dica)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CleanSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CleanBorder))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column {
                    Text(
                        text = "Dica para o Impostor",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CleanTextPrimary
                    )
                    Text(
                        text = gameState.hintMode.description,
                        fontSize = 13.sp,
                        color = CleanTextSecondary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HintMode.values().forEach { mode ->
                        val isSelected = mode == gameState.hintMode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) CleanAccent else CleanSurfaceVariant)
                                .clickable { onUpdateHintMode(mode) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mode.label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color.White else CleanTextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Seletor Multi-Tema
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Temas (Selecione 1 ou mais)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CleanTextPrimary
                )
                Text(
                    text = "${gameState.selectedCategoryIds.size} selecionado(s)",
                    fontSize = 12.sp,
                    color = CleanTextSecondary
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(availableCategories) { category ->
                    val isSelected = gameState.selectedCategoryIds.contains(category.id)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) CleanAccent else CleanSurface)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) CleanAccent else CleanBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onToggleCategory(category.id) }
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSelected) "✓ ${category.title}" else category.title,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) Color.White else CleanTextPrimary
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = { showAddWordDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CleanTextPrimary),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(CleanBorder)
                )
            ) {
                Text(
                    text = if (customItemCount > 0) "+ Adicionar Palavras Personalizadas ($customItemCount)" else "+ Adicionar Mais Palavras (PT-BR)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f, fill = false))

        // Botão Iniciar Partida
        Button(
            onClick = onStartGame,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CleanAccent,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = "Iniciar Partida",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    if (showEditNamesDialog) {
        EditPlayerNamesDialog(
            playerNames = gameState.playerNames,
            onNameChange = onUpdatePlayerName,
            onDismiss = { showEditNamesDialog = false }
        )
    }

    if (showAddWordDialog) {
        AddWordDialog(
            onSave = { word, hint ->
                onAddWord(word, hint)
                showAddWordDialog = false
            },
            onDismiss = { showAddWordDialog = false }
        )
    }
}

// Modal: Editar Nomes
/**
 * Diálogo modal para edição personalizada dos nomes dos participantes.
 *
 * Exibe uma lista com rolagem independente onde cada jogador pode ter seu
 * nome customizado (ex: "Gleison", "Mariana", etc.), mantendo a interface
 * limpa e sem poluição na tela principal de configuração.
 *
 * @param playerNames Lista dos nomes atuais dos jogadores.
 * @param onNameChange Callback executado a cada alteração de caractere no campo de texto.
 * @param onDismiss Callback disparado ao fechar ou salvar o diálogo.
 */
@Composable
fun EditPlayerNamesDialog(
    playerNames: List<String>,
    onNameChange: (Int, String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CleanSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Nomes dos Participantes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CleanTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Personalize o nome de cada jogador para a rodada:",
                        fontSize = 13.sp,
                        color = CleanTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(playerNames) { index, name ->
                        OutlinedTextField(
                            value = name,
                            onValueChange = { onNameChange(index, it) },
                            singleLine = true,
                            label = { Text("Jogador ${index + 1}", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CleanAccent,
                                unfocusedBorderColor = CleanBorder
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CleanAccent,
                        contentColor = Color.White
                    )
                ) {
                    Text("Concluir e Salvar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// Modal: Adicionar Palavra e Dica de 1 Palavra
/**
 * Diálogo modal para cadastro de novas palavras personalizadas em PT-BR.
 *
 * Solicita a Palavra Secreta e uma única palavra para servir de pista difícil ao impostor.
 * Os dados são persistidos localmente no SharedPreferences do Android.
 *
 * @param onSave Callback acionado com a palavra e a dica validadas.
 * @param onDismiss Callback acionado ao cancelar o cadastro.
 */
@Composable
fun AddWordDialog(
    onSave: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var word by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CleanSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column {
                    Text(
                        text = "Nova Palavra (PT-BR)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CleanTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Cadastre a palavra secreta e 1 única palavra de pista para o impostor:",
                        fontSize = 13.sp,
                        color = CleanTextSecondary
                    )
                }

                OutlinedTextField(
                    value = word,
                    onValueChange = { word = it },
                    singleLine = true,
                    label = { Text("Palavra Secreta (ex: Pipoca)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CleanAccent,
                        unfocusedBorderColor = CleanBorder
                    )
                )

                OutlinedTextField(
                    value = hint,
                    onValueChange = { hint = it },
                    singleLine = true,
                    label = { Text("Pista de 1 Palavra (ex: Cinema)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CleanAccent,
                        unfocusedBorderColor = CleanBorder
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (word.isNotBlank()) {
                                onSave(word, hint)
                            }
                        },
                        enabled = word.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CleanAccent,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}

// Contador
/**
 * Componente reutilizável de incremento/decremento com estética minimalista.
 *
 * Utilizado para controle de jogadores e impostores. Inclui validação de limites
 * mínimos e máximos com feedback visual nos botões (+ / -).
 *
 * @param title Título do contador (ex: "Jogadores", "Impostores").
 * @param subtitle Explicação ou restrição da regra em texto secundário.
 * @param value Valor numérico atual.
 * @param minValue Valor mínimo permitido.
 * @param maxValue Valor máximo permitido.
 * @param onValueChange Callback acionado ao pressionar os botões de incremento/decremento.
 */
@Composable
fun CleanCounterCard(
    title: String,
    subtitle: String,
    value: Int,
    minValue: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CleanSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CleanBorder))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CleanTextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = CleanTextSecondary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                IconButton(
                    onClick = { if (value > minValue) onValueChange(value - 1) },
                    enabled = value > minValue,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (value > minValue) CleanSurfaceVariant else Color.Transparent)
                ) {
                    Text(
                        text = "−",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (value > minValue) CleanTextPrimary else CleanTextSecondary.copy(alpha = 0.4f)
                    )
                }

                Text(
                    text = value.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CleanTextPrimary,
                    modifier = Modifier.widthIn(min = 20.dp),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = { if (value < maxValue) onValueChange(value + 1) },
                    enabled = value < maxValue,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (value < maxValue) CleanSurfaceVariant else Color.Transparent)
                ) {
                    Text(
                        text = "+",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (value < maxValue) CleanTextPrimary else CleanTextSecondary.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

// ==========================================
// TELA 2: DISTRIBUIÇÃO SECRETA (Passa o Celular)
// ==========================================
/**
 * Tela 2: Distribuição Secreta dos Papéis ("Passa o Celular").
 *
 * Mecânica de Sigilo:
 * - O jogador da vez pega o smartphone.
 * - Mantém o cartão pressionado para revelar a identidade (Palavra Secreta ou Impostor + Pista).
 * - Ao soltar o dedo, o cartão volta imediatamente ao estado oculto.
 * - Para avançar, utiliza a barra deslizante SwipeToConfirmBar e modal de confirmação,
 *   garantindo que o aparelho não passe de turno sozinho ou por toques involuntários.
 *
 * @param player Papel do jogador atual (índice, nome e flag de impostor).
 * @param category Título do tema selecionado na rodada.
 * @param secretWord Palavra secreta sorteada para os civis.
 * @param secretHint Dica enigmática de 1 palavra sorteada para o impostor.
 * @param hintMode Modo de dica ativo na partida.
 * @param isLastPlayer Flag indicando se é o último jogador a visualizar o papel.
 * @param nextPlayerName Nome do próximo participante a receber o aparelho.
 * @param onNextPlayerConfirmed Callback disparado após confirmação do avanço de turno.
 */
@Composable
fun PassPhoneScreen(
    player: PlayerRole,
    category: String,
    secretWord: String,
    secretHint: String,
    hintMode: HintMode,
    isLastPlayer: Boolean,
    nextPlayerName: String?,
    onNextPlayerConfirmed: () -> Unit
) {
    var isRevealed by remember(player.playerIndex) { mutableStateOf(false) }
    var showConfirmDialog by remember(player.playerIndex) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Indicador de Turno
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Surface(
                color = CleanSurfaceVariant,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Vez de Revelar",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = CleanTextSecondary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = player.playerName,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = CleanTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Pegue o aparelho em mãos e certifique-se de que ninguém está olhando.",
                fontSize = 14.sp,
                color = CleanTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Cartão Interativo: Segurar para Revelar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, CleanBorder, RoundedCornerShape(24.dp))
                .pointerInput(player.playerIndex) {
                    detectTapGestures(
                        onPress = {
                            isRevealed = true
                            tryAwaitRelease()
                            isRevealed = false
                        }
                    )
                },
            colors = CardDefaults.cardColors(
                containerColor = if (isRevealed) CleanSurface else CleanSurfaceVariant
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!isRevealed) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Pressione e segure",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CleanTextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mantenha o dedo pressionado na tela para visualizar sua identidade",
                            fontSize = 13.sp,
                            color = CleanTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = CleanSurfaceVariant,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Tema: $category",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = CleanTextSecondary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if (player.isImpostor) {
                            Text(
                                text = "VOCÊ É O IMPOSTOR",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = CleanImpostorAlert,
                                textAlign = TextAlign.Center,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            if (hintMode == HintMode.ONE_WORD && secretHint.isNotBlank()) {
                                Surface(
                                    color = CleanHintHighlight.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "💡 Pista (1 palavra):",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CleanHintHighlight
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = secretHint,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CleanTextPrimary,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            Text(
                                text = "Descubra a palavra através das perguntas dos outros sem ser notado!",
                                fontSize = 12.sp,
                                color = CleanTextSecondary,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = "Palavra Secreta",
                                fontSize = 13.sp,
                                color = CleanTextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = secretWord,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = CleanTextPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Slider com Trava Anti-Avanço
        SwipeToConfirmBar(
            text = if (isLastPlayer) "Deslize para iniciar debate ➔" else "Deslize para passar vez ➔",
            onConfirmed = {
                showConfirmDialog = true
            }
        )
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = if (isLastPlayer) "Iniciar Discussão?" else "Passar o Celular?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = if (isLastPlayer) {
                        "Todos os participantes viram seus papéis. Pronto para iniciar o debate?"
                    } else {
                        "Entregue o celular nas mãos de ${nextPlayerName ?: "próximo jogador"} e confirme para continuar."
                    },
                    fontSize = 14.sp,
                    color = CleanTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        onNextPlayerConfirmed()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CleanAccent,
                        contentColor = Color.White
                    )
                ) {
                    Text("Sim, Continuar", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmDialog = false },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Ainda não")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = CleanSurface
        )
    }
}

// Slider SwipeToConfirmBar
/**
 * Componente interativo de confirmação por deslizamento horizontal (Swipe to Confirm).
 *
 * Impede avanços acidentais ao passar o celular entre os jogadores.
 * O usuário deve arrastar o botão circular até atingir pelo menos 85% do trilho.
 * Caso seja solto antes do final, uma mola retorna o botão à posição inicial suavemente.
 *
 * @param text Texto explicativo exibido no fundo do trilho.
 * @param onConfirmed Callback acionado assim que o arrasto atinge o fim do curso.
 */
@Composable
fun SwipeToConfirmBar(
    text: String,
    onConfirmed: () -> Unit
) {
    val density = LocalDensity.current
    val barHeight = 56.dp
    val thumbSize = 48.dp

    var offsetX by remember { mutableStateOf(0f) }
    var maxDragPx by remember { mutableStateOf(0f) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight)
            .clip(RoundedCornerShape(24.dp))
            .background(CleanSurfaceVariant)
            .border(1.dp, CleanBorder, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        val totalWidthPx = with(density) { maxWidth.toPx() }
        val thumbSizePx = with(density) { thumbSize.toPx() }
        maxDragPx = (totalWidthPx - thumbSizePx - with(density) { 8.dp.toPx() }).coerceAtLeast(0f)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = CleanTextSecondary
            )
        }

        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .size(thumbSize)
                .clip(CircleShape)
                .background(CleanAccent)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newOffset = (offsetX + delta).coerceIn(0f, maxDragPx)
                        offsetX = newOffset
                    },
                    onDragStopped = {
                        if (offsetX >= maxDragPx * 0.85f) {
                            offsetX = 0f
                            onConfirmed()
                        } else {
                            offsetX = 0f
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "➔",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ==========================================
// TELA 3: FASE DE DISCUSSÃO
// ==========================================
/**
 * Tela 3: Fase de Discussão e Debate.
 *
 * Exibida após todos os participantes terem visualizado seus papéis em sigilo.
 * Realiza o sorteio e destaca o primeiro jogador responsável por iniciar a rodada
 * de perguntas investigativas para que ninguém fique tímido ou sem saber quem começa.
 *
 * @param startingPlayer Nome do participante sorteado para abrir a rodada de perguntas.
 * @param onFinishDiscussion Callback para finalizar o debate e avançar à revelação dos papéis.
 */
@Composable
fun DiscussionScreen(
    startingPlayer: String,
    onFinishDiscussion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 28.dp)
        ) {
            Surface(
                color = CleanSurfaceVariant,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Hora do Debate",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = CleanTextSecondary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Fase de Discussão",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = CleanTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Todos os jogadores já viram seus papéis. Façam perguntas entre si para desmascarar os impostores!",
                fontSize = 14.sp,
                color = CleanTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CleanSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CleanBorder))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Primeiro a perguntar:",
                    fontSize = 14.sp,
                    color = CleanTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = startingPlayer,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = CleanTextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Faça uma pergunta sobre o tema para qualquer outro participante.",
                    fontSize = 13.sp,
                    color = CleanTextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Button(
            onClick = onFinishDiscussion,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CleanAccent,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = "Encerrar e Revelar Papéis",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ==========================================
// TELA 4: REVELAÇÃO FINAL
// ==========================================
/**
 * Tela 4: Revelação Final dos Papéis e Encerramento.
 *
 * Apresenta a resolução da rodada:
 * - A palavra secreta correta e o tema.
 * - A pista de 1 palavra fornecida aos impostores (para validação dos blefes).
 * - A lista com os nomes de todos os impostores da partida.
 * - Botões para "Jogar Novamente" (mantendo nomes e configurações) ou "Configurar Nova Partida".
 *
 * @param secretWord Palavra secreta da partida finalizada.
 * @param secretHint Dica de 1 palavra concedida ao impostor.
 * @param hintMode Modo de dica que esteve ativo.
 * @param category Nome do tema da rodada.
 * @param impostors Lista com os nomes dos jogadores que eram impostores.
 * @param onPlayAgain Callback para reiniciar a partida com nova palavra sorteada.
 * @param onBackToSetup Callback para retornar à tela inicial de configurações.
 */
@Composable
fun RevealScreen(
    secretWord: String,
    secretHint: String,
    hintMode: HintMode,
    category: String,
    impostors: List<String>,
    onPlayAgain: () -> Unit,
    onBackToSetup: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Surface(
                color = CleanSurfaceVariant,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Fim da Rodada",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = CleanTextSecondary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Resultado",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = CleanTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Card com Palavra e Pista de 1 palavra
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CleanSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CleanBorder))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tema: $category",
                    fontSize = 14.sp,
                    color = CleanTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = secretWord,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = CleanTextPrimary
                )

                if (hintMode == HintMode.ONE_WORD && secretHint.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = CleanHintHighlight.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Pista dada ao impostor: \"$secretHint\"",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CleanTextPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card com os Impostores
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CleanSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CleanBorder))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (impostors.size > 1) "Os Impostores eram:" else "O Impostor era:",
                    fontSize = 14.sp,
                    color = CleanTextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = impostors.joinToString(", "),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = CleanImpostorAlert,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Botões de Ação
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onPlayAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CleanAccent,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "Jogar Novamente",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            OutlinedButton(
                onClick = onBackToSetup,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CleanTextPrimary
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(CleanBorder)
                )
            ) {
                Text(
                    text = "Configurar Nova Partida",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

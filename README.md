# O Impostor — Jogo de Dedução Social & Blefe (Android)

Aplicativo Android nativo completo para o clássico jogo de dedução social no formato **"passa-o-celular"** (inspirado na dinâmica de jogos como *Spyfall* e *jogodoimpostor.com.br*).

Construído em **Kotlin** com **Jetpack Compose + Material 3**, o app preza por uma experiência estritamente minimalista, design clean e moderna engenharia de software sem dependências de bibliotecas de terceiros pesadas.

---

## Sumário
- [Visão Geral & Mecânica](#visão-geral--mecânica)
- [Funcionalidades Principais](#funcionalidades-principais)
- [Design System & UI Clean](#design-system--ui-clean)
- [Estrutura do Banco de Palavras & Dicas](#estrutura-do-banco-de-palavras--dicas)
- [Arquitetura Técnica & Código](#arquitetura-técnica--código)
- [Estrutura de Diretórios](#estrutura-de-diretórios)
- [Como Compilar e Gerar o APK](#como-compilar-e-gerar-o-apk)
- [Assinatura Digital (Keystore)](#assinatura-digital-keystore)

---

## Visão Geral & Mecânica

O jogo é disputado em grupo (mínimo de 3 pessoas) utilizando **um único smartphone** que circula entre os jogadores:

1. **Configuração**: Define-se a quantidade de jogadores, quantidade de impostores, os temas ativos e a dificuldade da dica.
2. **Distribuição Secreta ("Passa o Celular")**: Cada jogador pega o aparelho, pressiona e segura o dedo na tela para ver seu papel:
   - **Civil**: Vê o Tema e a **Palavra Secreta** (ex: *Tema: Comidas*, *Palavra: Pizza*).
   - **Impostor**: Vê o Tema, o aviso **"VOCÊ É O IMPOSTOR"** e uma **dica difícil de apenas 1 palavra** (ex: *Fatia*).
3. **Trava Anti-Avanço**: Para passar a vez para o próximo jogador, o participante precisa deslizar uma barra lateral (*Swipe*) e confirmar na tela, impedindo que o celular passe de turno por acidente.
4. **Fase de Debate**: O aplicativo sorteia aleatoriamente quem começa perguntando. O grupo faz perguntas entre si tentando descobrir quem não sabe a palavra, enquanto os impostores tentam blefar e deduzir a palavra real.
5. **Revelação**: Encerra-se a partida exibindo a palavra real, as pistas entregues aos impostores e revelando quem eram os blefadores.

---

## Funcionalidades Principais

- **Seleção de Múltiplos Temas**: Permite marcar 1 ou mais categorias simultaneamente (ex: *Comidas* + *Filmes* + *Lugares*). O jogo sorteia palavras do conjunto escolhido.
- **Dicas Difíceis de 1 Única Palavra**: 
  - Baseadas em referências de dedução social para o impostor não ficar completamente perdido, mas sem entregar o item.
  - Ex: *Titanic ➔ Iceberg*, *Hambúrguer ➔ Chapa*, *Sushi ➔ Alga*, *Paris ➔ Ferro*, *Leão ➔ Juba*.
- **Modos de Dica**:
  - `Palavra Única`: O impostor recebe 1 palavra relacionada indiretamente.
  - `Sem Dica`: Modo purista clássico onde o impostor não recebe nenhuma pista.
- **Edição de Nomes dos Participantes**: Possibilidade de renomear os jogadores (*ex: Gleison, Lucas, Maria*) que são refletidos no jogo e no sorteio.
- **Trava de Segurança (Swipe to Confirm)**: Elimina o risco de a tela passar de jogador antes do celular ser devidamente entregue nas mãos do próximo.
- **Adicionar Novas Palavras (PT-BR)**: Permite cadastrar palavras personalizadas com dica diretamente pelo app, com persistência local via `SharedPreferences`.
- **Acompanha Planilha Excel**: Arquivo `palavras_e_dicas_impostor.xlsx` contendo as 376 palavras categorizadas e com explicação da relação indireta.

---

## Design System & UI Clean

O app segue rigorosamente as diretrizes de visual minimalista e calmo:
- **Paleta de Cores**:
  - Fundo principal: `CleanBackground` (`#F7F8FA`)
  - Cartões e Superfícies: `CleanSurface` (`#FFFFFF`) / `CleanSurfaceVariant` (`#EFF2F5`)
  - Tipografia primária: `CleanTextPrimary` (`#1E232A` — Grafite escuro)
  - Tipografia secundária: `CleanTextSecondary` (`#6B7280` — Cinza neutro)
  - Cor de destaque/ação: `CleanAccent` (`#334155` — Slate sofisticado)
  - Bordas: `CleanBorder` (`#E2E8F0` — Linhas finas)
  - Alerta do Impostor: `CleanImpostorAlert` (`#DC2626` — Vermelho sutil)
  - Destaque de Pista: `CleanHintHighlight` (`#D97706` — Âmbar discreto)
- **Cantos Arredondados**: Todos os componentes, cartões e botões utilizam `RoundedCornerShape` entre 16.dp e 24.dp.

---

## Estrutura do Banco de Palavras & Dicas

O banco interno do código (`WordRepository`) conta com **376 palavras e dicas em português**, distribuídas em **11 categorias temáticas**:

| Categoria | ID | Qtd. Palavras | Exemplos (Palavra ➔ Dica) |
| :--- | :--- | :---: | :--- |
| **Comidas & Bebidas** | `comidas_e_bebidas` | 50 | *Pizza ➔ Fatia*, *Miojo ➔ Minutos*, *Pão de Queijo ➔ Polvilho* |
| **Animais** | `animais` | 45 | *Leão ➔ Juba*, *Ornitorrinco ➔ Bico*, *Polvo ➔ Tinta* |
| **Objetos do Dia a Dia** | `objetos_do_dia_a_dia` | 44 | *Celular ➔ Tela*, *Garrafa Térmica ➔ Vácuo*, *Mochila ➔ Zíper* |
| **Profissões** | `profissoes` | 44 | *Médico ➔ Jaleco*, *Astronauta ➔ Órbita*, *Barbeiro ➔ Navalha* |
| **Lugares do Mundo** | `lugares_do_mundo` | 44 | *Paris ➔ Ferro*, *Veneza ➔ Gôndola*, *Taj Mahal ➔ Mármore* |
| **Hobbies & Esportes** | `hobbies_e_esportes` | 43 | *Futebol ➔ Trave*, *Skate ➔ Lixa*, *Xadrez ➔ Xeque* |
| **Lugares & Ambientes** | `lugares_e_ambientes` | 40 | *Hospital ➔ Maca*, *Biblioteca ➔ Silêncio*, *Prisão ➔ Grade* |
| **Filmes & Séries** | `filmes_e_series` | 36 | *Titanic ➔ Iceberg*, *Matrix ➔ Pílula*, *O Iluminado ➔ Machado* |
| **Tecnologia & Geek** | `tecnologia_e_geek` | 10 | *Wi-Fi ➔ Ondas*, *Processador ➔ Silício*, *Satélite ➔ Órbita* |
| **Natureza & Fenômenos**| `natureza_e_fenomenos`| 10 | *Terremoto ➔ Fissura*, *Aurora Boreal ➔ Magnetismo* |
| **Mitologia & Ficção** | `mitologia_e_ficcao` | 10 | *Dragão ➔ Escama*, *Vampiro ➔ Espelho*, *Fênix ➔ Cinzas* |

---

## Arquitetura Técnica & Código

- **Single Activity**: Padrão moderno onde o fluxo de telas (`SETUP`, `PASS_PHONE`, `DISCUSSION`, `REVEAL`) é orquestrado de forma reativa via `Crossfade` no Compose.
- **Gerenciamento de Estado**: `GameState` imutável manipulado com `remember { mutableStateOf(...) }`.
- **Sensibilidade a Toque**: A visualização secreta utiliza `pointerInput` com `detectTapGestures(onPress = ...)` garantindo que o conteúdo suma instantaneamente ao soltar o dedo (`tryAwaitRelease()`).
- **Gesto de Arraste (Swipe)**: Utiliza `draggable(orientation = Orientation.Horizontal)` com animação de retorno automático se o limite de 85% do trilho não for atingido.

---

## Estrutura de Diretórios

```
jogo/
├── README.md                            # Documentação completa do projeto
├── build.gradle.kts                     # Gradle do projeto raiz
├── settings.gradle.kts                  # Configurações do projeto e inclusão do módulo :app
├── gradle.properties                    # Configurações da JVM e AndroidX
├── local.properties                     # Caminho local do Android SDK
├── keystore.properties                  # Parâmetros de assinatura digital
├── release.jks                          # Certificado/Keystore oficial
├── gradlew & gradlew.bat                # Scripts do Gradle Wrapper
├── palavras_e_dicas_impostor.xlsx       # Planilha com as 376 palavras catalogadas
├── gradle/
│   └── libs.versions.toml               # Catálogo de versões e dependências
└── app/
    ├── build.gradle.kts                 # Dependências do Compose, compilação e signingConfigs
    └── src/main/
        ├── AndroidManifest.xml          # Manifesto do aplicativo
        └── java/com/exemplo/oimpostor/
            └── MainActivity.kt          # UI, Lógica, Estados e Banco de Palavras
```

---

## Como Compilar e Gerar o APK

O projeto foi configurado com o Gradle Wrapper autônomo, dispensando obrigatoriamente a interface do Android Studio:

### 1. Gerar APK de Release (Assinado)
```powershell
.\gradlew.bat assembleRelease
```
O APK final será gerado em:
`app/build.outputs/apk/release/app-release.apk`

### 2. Gerar APK de Debug
```powershell
.\gradlew.bat assembleDebug
```
O APK de teste será gerado em:
`app/build.outputs/apk/debug/app-debug.apk`

### 3. Instalar Diretamente no Dispositivo Conectado
Com o celular conectado via USB e com a depuração ativada:
```powershell
.\gradlew.bat installRelease
```

---

## Assinatura Digital (Keystore)

O aplicativo foi assinado e verificado com a seguinte chave:
- **Keystore**: `release.jks`
- **Alias**: `sousa03g`
- **Titular (DName)**: `CN=Sousa03g, OU=GitHub, O=Sousa03g, L=Brazil, ST=BR, C=BR`
- **Algoritmo**: SHA384withRSA (2048-bit)
- **Validade**: Até Fevereiro de 2054
- **Assinatura Verificada**: Esquema v2 do Android (`apksigner: Verifies = true`)

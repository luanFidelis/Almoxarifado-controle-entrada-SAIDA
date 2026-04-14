let listaGlobalMateriais = []; 
let filtroAtual = 'TODOS';     

// --- INICIALIZAÇÃO ---
document.addEventListener('DOMContentLoaded', () => {
    carregarListaMateriais();

    // Captura os parâmetros da URL (Caso a página seja aberta pelo QR Code)
    const urlParams = new URLSearchParams(window.location.search);
    const idDoQrCode = urlParams.get('id');

    if (idDoQrCode) {
        // Se a página foi aberta pelo QR Code e a função existir
        if (typeof atualizarStatusProduto === "function") {
            atualizarStatusProduto(idDoQrCode);
        }
    } else {
        // Se a pessoa abriu a página normalmente e a função de câmera existir
        if (typeof iniciarCamera === "function") {
            iniciarCamera();
        }
    }
});

// --- CARREGAMENTO E BUSCA ---
async function carregarListaMateriais() {
    try {
        const response = await fetch('http://localhost:8080/v1/Warehouse/show_list');
        if (response.ok) {
            listaGlobalMateriais = await response.json();
            aplicarFiltroAtual();
        }
    } catch (error) {
        mostrarErro("Erro de conexão com a API.");
    }
}

async function buscarMateriais() {
    const inputName = document.getElementById('inputBuscaName').value.trim();
    if (!inputName) {
        carregarListaMateriais();
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/v1/Warehouse/filter', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: inputName })
        });

        if (response.ok) {
            const data = await response.json();
            renderizarTabela([data]); 
        } else if (response.status === 404) {
            document.getElementById('tabelaMateriais').innerHTML = `<tr><td colspan="5" class="empty-message">Nenhum material encontrado com este nome.</td></tr>`;
        }
    } catch (error) {
        mostrarErro("Não foi possível conectar à API de busca.");
    }
}

// --- FILTROS E RENDERIZAÇÃO DA TABELA ---
function filtrarMateriais(filtro, botaoClicado) {
    const pills = document.querySelectorAll('.pill');
    pills.forEach(pill => pill.classList.remove('active'));
    botaoClicado.classList.add('active');
    filtroAtual = filtro;
    aplicarFiltroAtual();
}

function aplicarFiltroAtual() {
    if (filtroAtual === 'TODOS') {
        renderizarTabela(listaGlobalMateriais);
    } else {
        const listaFiltrada = listaGlobalMateriais.filter(item => item.status === filtroAtual);
        renderizarTabela(listaFiltrada);
    }
}

function renderizarTabela(materiais) {
    const tbody = document.getElementById('tabelaMateriais');
    tbody.innerHTML = ''; 
    
    if (!materiais || materiais.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" class="empty-message">Nenhum material disponível no momento.</td></tr>`;
        return;
    }

    materiais.forEach(item => {
        let statusHtml = '';
        if (item.status === 'AVAILABLE') statusHtml = '<div class="status-badge" style="background: #dcfce7; color: #166534; border: 1px solid #bbf7d0;">DISPONÍVEL</div>';
        else if (item.status === 'IN_USE') statusHtml = '<div class="status-badge" style="background: #e0e7ff; color: #3730a3; border: 1px solid #c7d2fe;">EM USO</div>';
        else if (item.status === 'UNAVAILABLE') statusHtml = '<div class="status-badge" style="background: #fee2e2; color: #991b1b; border: 1px solid #fecaca;">INDISPONÍVEL</div>';

        let movimentoTexto = '';
        if (item.typeofmoviment === 'EXIT') movimentoTexto = '<span style="color: #059669;">●</span> Saída Liberada';
        else if (item.typeofmoviment === 'PROHIBITED') movimentoTexto = '<span style="color: #dc2626;">●</span> Movimentação Proibida';

        let imgTag = item.imageUrl 
            ? `<img src="${item.imageUrl}" class="item-img" alt="Foto">` 
            : `<div class="item-img" style="display:flex; align-items:center; justify-content:center; color:#94a3b8; font-size:0.75rem;">Sem Foto</div>`;

        const safeName = item.name.replace(/'/g, "\\'");

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td><input type="checkbox"></td>
            <td>
                <div class="item-info">
                    ${imgTag}
                    <div>
                        <div class="item-name">${item.name}</div>
                        ${statusHtml}
                    </div>
                </div>
            </td>
            <td><div class="item-category">${movimentoTexto}</div></td>
            <td style="font-weight: 500; color: var(--text-main);">-</td>
            <td>
                <div class="action-buttons" style="justify-content: flex-end;">
                    <button class="btn-action" onclick="abrirModalEditar(${item.id}, '${item.status}')" title="Editar Status">✏️</button>
                    <button class="btn-action" onclick="abrirModalQR(${item.id}, '${safeName}')" title="Gerar QR Code">QR</button>
                    <button class="btn-action btn-delete" onclick="deletarMaterial(${item.id})" title="Excluir">🗑️</button>
                </div>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// --- CADASTRO DE MATERIAIS ---
function abrirModal() { document.getElementById('modalCadastro').style.display = 'flex'; }

function fecharModal() {
    document.getElementById('modalCadastro').style.display = 'none';
    document.getElementById('cadNome').value = '';
    document.getElementById('cadMovimento').value = 'EXIT';
    document.getElementById('cadStatus').value = 'AVAILABLE';
    document.getElementById('cadImagem').value = ''; 
}

async function salvarMaterial() {
    // 1. Captura os valores digitados no modal
    const nome = document.getElementById('cadNome').value.trim();
    const movimento = document.getElementById('cadMovimento').value;
    const status = document.getElementById('cadStatus').value;
    
    if (!nome) { 
        alert("Preencha o nome do material!"); 
        return; 
    }

    // 2. Faz o upload da imagem e pega a URL
    const urlGerada = await enviarImagemParaServidor(document.getElementById('cadImagem'));
    
    // 3. AQUI ESTÁ O warehouseDTO! 
    const warehouseDTO = { 
        name: nome, 
        typeofmoviment: movimento, 
        status: status, 
        imageUrl: urlGerada 
    };

    try {
        // 4. Dispara a criação na tabela Warehouse
        const resWarehouse = await fetch('http://localhost:8080/v1/Warehouse/add_product', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(warehouseDTO)
        });

        if (resWarehouse.ok) {
            // Pega a resposta do backend (precisa conter o ID gerado)
            const materialCriado = await resWarehouse.json(); 

            // 5. Monta o DTO para o History usando o ID novo
            const historyDTO = {
               
                name: nome,
                state: status,
                colaboratorName: null 
            };

            // 6. Dispara a criação na tabela History
            // OBS: Verifique se a sua rota de criação no history é essa mesma
            await fetch('http://localhost:8080/v2/History/add', { 
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(historyDTO)
            });

            // Finaliza fechando o modal e recarregando a tabela
            fecharModal(); 
            carregarListaMateriais(); 
        }
    } catch (error) { 
        alert("Erro de conexão ao salvar material."); 
    }
}

// --- EDIÇÃO DE MATERIAIS ---
function abrirModalEditar(id, statusAtual) {
    document.getElementById('editId').value = id;
    document.getElementById('editStatusOriginal').value = statusAtual; 
    document.getElementById('editStatus').value = statusAtual; 
    
    // Chama a verificação para exibir/esconder o campo de colaborador dependendo do status atual
    verificarStatusColaborador(); 
    
    document.getElementById('modalEditar').style.display = 'flex';
}

function fecharModalEditar() { 
    document.getElementById('modalEditar').style.display = 'none'; 
}

function verificarStatusColaborador() {
    const status = document.getElementById('editStatus').value;
    const divColaborador = document.getElementById('div-colaborador');
    if (status === 'IN_USE') {
        divColaborador.style.display = 'block';
    } else {
        divColaborador.style.display = 'none';
        document.getElementById('editColaborador').value = ''; // Limpa o campo se não for IN_USE
    }
}

async function salvarEdicao() {
    // 1. Captura os valores do modal
    const id = parseInt(document.getElementById('editId').value);
    const novoStatus = document.getElementById('editStatus').value;
    const nomeColaborador = document.getElementById('editColaborador').value;
    const inputFoto = document.getElementById('editImagem');
    
    // 2. Validação: Exige o nome do funcionário se o status for "Em Uso"
    if (novoStatus === 'IN_USE' && nomeColaborador.trim() === '') {
        alert("Por favor, informe o nome do funcionário que está usando o material.");
        return;
    }

    let sucesso = false;

    // 3. Atualizar Imagem se selecionada
    if (inputFoto && inputFoto.files.length > 0) {
        const url = await enviarImagemParaServidor(inputFoto);
        if (url) {
            await fetch('http://localhost:8080/v1/Warehouse/editar/imagem', {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id, imageUrl: url })
            });
            sucesso = true;
        }
    }

    // 4. Montar os DTOs com os nomes exatos
    const warehouseDTO = {
        id: id,
        status: novoStatus
    };

    const updateDto = {
        id: id,
        colaborattorName: nomeColaborador,
        state: novoStatus
    };

    try {
        // 5. Dispara para a v1 (Warehouse) como POST
        const resWarehouse = await fetch('http://localhost:8080/v1/Warehouse/editar/status', {
            method: 'POST', 
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(warehouseDTO)
        });

        // 6. Dispara para a v2 (History) como POST
        const resHistory = await fetch('http://localhost:8080/v2/History/editStatus', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updateDto)
        });

        // 7. Checa se as duas deram 200 OK
        if (resWarehouse.ok && resHistory.ok) {
            sucesso = true;
            alert('Material atualizado com sucesso!');
        } else {
            console.error('Status Warehouse:', resWarehouse.status, 'Status History:', resHistory.status);
            alert('Erro: O material não foi encontrado em um dos bancos (Warehouse ou History).');
        }
    } catch (error) {
        console.error('Erro na requisição dupla:', error);
        alert('Erro de conexão ao salvar status.');
    }

    // 8. Finaliza
    if (sucesso) { 
        fecharModalEditar(); 
        carregarListaMateriais(); 
    }
}

// --- EXCLUSÃO DE MATERIAIS ---
async function deletarMaterial(id) {
    if (!confirm("Tem certeza que deseja excluir este material permanentemente?")) return; 
    try {
        const response = await fetch(`http://localhost:8080/v1/Warehouse/delete/${id}`, { method: 'DELETE' });
        if (response.ok) carregarListaMateriais();
    } catch (error) { alert("Erro ao excluir o material."); }
}

// --- FUNÇÕES DE QR CODE COM IMPRESSÃO ---
async function abrirModalQR(id, nome) {
    const modal = document.getElementById('modalQR');
    const imgEl = document.getElementById('imgQrCode');
    const loadingText = document.getElementById('qrLoadingText');
    const btnImprimir = document.getElementById('btnImprimirQR');
    
    document.getElementById('qrNomeMaterial').innerText = nome;
    imgEl.style.display = 'none';
    btnImprimir.style.display = 'none';
    loadingText.style.display = 'block';
    loadingText.innerText = 'Gerando QR Code...';
    modal.style.display = 'flex';

    try {
        const response = await fetch(`http://localhost:8080/v1/Warehouse/qrcode/${id}`);
        if (response.ok) {
            const data = await response.json();
            imgEl.src = data.imagemQrCode;
            loadingText.style.display = 'none';
            imgEl.style.display = 'block';
            btnImprimir.style.display = 'inline-flex';
        } else {
            loadingText.innerText = 'Erro ao gerar QR Code.';
        }
    } catch (error) {
        loadingText.innerText = 'Erro de conexão com o servidor.';
    }
}

function fecharModalQR() {
    document.getElementById('modalQR').style.display = 'none';
    document.getElementById('imgQrCode').src = '';
}

function imprimirQRCode() {
    const imgSrc = document.getElementById('imgQrCode').src;
    const nomeMaterial = document.getElementById('qrNomeMaterial').innerText;
    if (!imgSrc) return;

    const janela = window.open('', '_blank', 'width=600,height=600');
    janela.document.write(`
        <html>
        <head>
            <title>Imprimir QR - ${nomeMaterial}</title>
            <style>
                body { display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; font-family: 'Inter', sans-serif; margin: 0; background: #fff; }
                img { width: 250px; margin-bottom: 24px; }
                h2 { font-size: 24px; color: #0f172a; margin: 0; }
                @media print { body { height: auto; margin-top: 50px; } }
            </style>
        </head>
        <body>
            <img src="${imgSrc}">
            <h2>${nomeMaterial}</h2>
            <script>
                setTimeout(() => { window.print(); window.close(); }, 500);
            <\/script>
        </body>
        </html>
    `);
    janela.document.close();
}

// --- UTILITÁRIOS ---
function mostrarErro(mensagem) {
    document.getElementById('tabelaMateriais').innerHTML = `<tr><td colspan="5" class="empty-message" style="color: var(--danger);">${mensagem}</td></tr>`;
}

async function enviarImagemParaServidor(inputElement) {
    if (!inputElement.files || inputElement.files.length === 0) return null; 
    const formData = new FormData();
    formData.append('file', inputElement.files[0]);
    try {
        const response = await fetch('http://localhost:8080/v1/Warehouse/upload', {
            method: 'POST',
            body: formData
        });
        return response.ok ? await response.text() : null;
    } catch (error) {
        console.error("Erro no upload:", error);
        return null;
    }
}
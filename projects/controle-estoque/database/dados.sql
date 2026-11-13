-- =====================================================================
--  Dados de exemplo (opcional). Execute depois do schema.sql.
-- =====================================================================

INSERT INTO categoria (nome, descricao) VALUES
    ('Informática', 'Periféricos e acessórios de computador'),
    ('Escritório',  'Material de papelaria e escritório'),
    ('Limpeza',     'Produtos de limpeza e higiene');

INSERT INTO fornecedor (nome, cnpj, email, telefone) VALUES
    ('TechDistribuidora Ltda', '11222333000181', 'vendas@techdistribuidora.com.br', '(11) 3333-1000'),
    ('Papelaria Central S.A.', '44555666000199', 'contato@papelariacentral.com.br', '(21) 2222-2000'),
    ('LimpaBem Comércio',      '77888999000155', 'pedidos@limpabem.com.br',         '(31) 3111-3000');

INSERT INTO produto (sku, nome, descricao, preco_unitario, quantidade_em_estoque, estoque_minimo, ativo, categoria_id) VALUES
    ('INF-MOUSE-01', 'Mouse óptico USB',         'Mouse com fio, 1200 DPI',      49.90, 0, 10, TRUE, 1),
    ('INF-TECL-01',  'Teclado ABNT2',            'Teclado com fio padrão ABNT2', 89.90, 0,  5, TRUE, 1),
    ('ESC-PAPEL-A4', 'Papel sulfite A4 (resma)', 'Resma com 500 folhas',         27.50, 0, 20, TRUE, 2),
    ('LIM-ALC-70',   'Álcool 70% 1L',            'Álcool etílico 70%',           12.90, 0, 15, TRUE, 3);

INSERT INTO produto_fornecedor (produto_id, fornecedor_id) VALUES
    (1, 1), (2, 1), (3, 2), (4, 3), (4, 2);

-- Movimentações iniciais (saldos coerentes com a quantidade do produto)
INSERT INTO movimentacao (produto_id, fornecedor_id, tipo, quantidade, saldo_anterior, saldo_posterior, data_hora, observacao) VALUES
    (1, 1, 'ENTRADA', 30,  0, 30, '2026-11-01 09:00:00', 'Compra inicial'),
    (1, NULL, 'SAIDA', 22, 30,  8, '2026-11-03 14:30:00', 'Requisição do setor de TI'),
    (2, 1, 'ENTRADA', 12,  0, 12, '2026-11-01 09:05:00', 'Compra inicial'),
    (3, 2, 'ENTRADA', 50,  0, 50, '2026-11-02 10:00:00', 'Compra inicial'),
    (4, 3, 'ENTRADA', 10,  0, 10, '2026-11-02 11:00:00', 'Compra inicial');

UPDATE produto SET quantidade_em_estoque = 8  WHERE id = 1;
UPDATE produto SET quantidade_em_estoque = 12 WHERE id = 2;
UPDATE produto SET quantidade_em_estoque = 50 WHERE id = 3;
UPDATE produto SET quantidade_em_estoque = 10 WHERE id = 4;

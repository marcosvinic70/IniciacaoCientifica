
# Exibe várias soluções do problema LocRSSF.
#
# Espera arquivos que tenham as coordenadas X e Y (um nó por linha).
#
# Os arquivos podem ser passados como argumento na linha de comando ou
# informados quando o programa é iniciado.
#
# Assume a princípio que a área da rede é 1 x 1
#
# @author Julio Cesar Alves
# @version 2016-12-08

import sys
import matplotlib.pyplot as plt

def obter_nomes_arquivos():
    '''Obtem os nomes dos arquivos a serem utilizados.
       Pega, a principio,os nomes passados como argumentos em linha de comando. 
       Caso não tenham sido passados, solicita ao usuario.
       Retorna uma litsa de nomes de arquivos'''
    if len(sys.argv) > 1:
        nomes_arqs = sys.argv[1:]
    else:
        n = int(input("Informe a quantidade de arquivos: "))
        nomes_arqs = []
        for i in range(n):
            nomes_arqs.append(input("Nome do arquivo " + str(i) + ": "))
    return nomes_arqs

def obter_area():
    '''Obtem a area das redes a serem exibidas.
       Considera a principio que area é 1 x 1. Usuario pode confirmar ou
       escolher outra área.
       Retorna o lado da area das redes (assume areas quadradas).'''
    lado_area = 1.0
    confirmar_area = input("Usar area 1x1? (ENTER para aceitar)")
    if confirmar_area != "":
        lado_area = float(input("Informe o lado da area: "))
    return lado_area

def le_arquivo_solucao(nome_arq):
    '''Le o arquivo de solucao passado por parametro, retornando uma tupla
       com duas listas uma com as coordenadas X e outra com as coordenadas Y'''
    arq = open(nome_arq)
    l_x = []
    l_y = []
    for linha in arq:
        x, y = map(float, linha.split())
        l_x.append(x)
        l_y.append(y)
    return l_x, l_y

def le_arquivo_rede(nome_arq):
    '''Le o arquivo de solucao passado por parametro, retornando uma tupla
    com duas listas uma com as coordenadas X e outra com as coordenadas Y'''
    arq = open(nome_arq)
    l_x = []
    l_y = []
    arq = arq.readlines()[3:]
    for linha in arq:
        id, x, y = map(float, linha.split())
        l_x.append(x)
        l_y.append(y)
    return l_x, l_y


# Subprograma principal
nomes_arquivos = obter_nomes_arquivos()
# lado_area = obter_area()

# cores de plotagem (primeira letra é a cor, segunda indica que é um círculo
estilos = ["bo", "rs", "gD", "cp", "m+", "kx"]
estilo_atual = 0
l_x, l_y = le_arquivo_solucao(nomes_arquivos[0])
plt.plot(l_x, l_y, estilos[0], fillstyle="none")
l_x, l_y = le_arquivo_rede(nomes_arquivos[1])
plt.plot(l_x, l_y, estilos[1], fillstyle="none")
# exibe as solucoes, uma com cada cor
# for nome_arq in nomes_arquivos:
#     l_x, l_y = le_arquivo_solucao(nome_arq)
#     plt.plot(l_x, l_y, estilos[estilo_atual], fillstyle="none")
#     estilo_atual = (estilo_atual + 1) % len(estilos)

plt.show()
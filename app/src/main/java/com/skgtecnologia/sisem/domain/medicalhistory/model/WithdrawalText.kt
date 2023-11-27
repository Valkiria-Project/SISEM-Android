package com.skgtecnologia.sisem.domain.medicalhistory.model

private const val PATIENT = "<Paciente>"
private const val DOCUMENT = "<Documento>"
private const val VEHICLE_CODE = "<Codigo>"
private const val RESPONSIBLE = "<Responsable>"
private const val RESPONSIBLE_DOCUMENT_TYPE = "<TipoDocumento>"
private const val RESPONSIBLE_DOCUMENT = "<Documento>"

private const val WITHDRAWAL_WITNESS_TEXT =
    "<p color=\"#FFFFFF\" align=\"justify\"><font color=\"#FFFFFF\">Obrando en representación" +
        " y/o tutor del paciente <b>$PATIENT</b> identificado con documento de identidad" +
        " <b>$DOCUMENT</b> <br>En forma LIBRE, ESPONTÁNEA Y VOLUNTARIAMENTE MANIFIESTO MI" +
        " NEGATIVA A ACEPTAR QUE SE REALICE EL TRASLADO EN EL VEHÍCULO DE EMERGENCIA" +
        " <b>$VEHICLE_CODE.</b> <br>Se me explicó clara y detalladamente que puede presentar" +
        " riesgos y posibles complicaciones para la salud en general de mi representado al" +
        " no ser valorado y trasladado. <br>En consecuencia, <b>NO AUTORIZO</b> la" +
        " realización de la valoración o el traslado a pesar del propósito, ventajas," +
        " posibles riesgos y consecuencias, que ayudan a determinar la mejoría o deterioro" +
        " de la enfermedad, los cuales fueron explicados clara y detalladamente por los" +
        " profesionales y el personal de tripulación de la AMBULANCIA. <br>Se me ha dado la" +
        " oportunidad de hacer preguntas y todas han sido contestadas satisfactoriamente." +
        "<br>Por lo anterior en forma consciente doy firma asumiendo toda la responsabilidad," +
        " exonerando al prestador del servicio y/o Centro Regulador de Urgencias y" +
        " Emergencia SDS y profesionales, del estado de salud de mi representado, por" +
        " las siguientes causas:</font></p>"

private const val WITHDRAWAL_RESPONSIBLE_TEXT =
    "<p color=\"#FFFFFF\" align=\"justify\"><font color=\"#FFFFFF\">Yo <b>$RESPONSIBLE</b>" +
        " Identificado" + " con <b>$RESPONSIBLE_DOCUMENT_TYPE</b> número " +
        "<b>$RESPONSIBLE_DOCUMENT</b> expedida en.</font></p>"

fun getWithdrawalWitnessText(patient: String, document: String, code: String): String =
    WITHDRAWAL_WITNESS_TEXT
        .replace(PATIENT, patient)
        .replace(DOCUMENT, document)
        .replace(VEHICLE_CODE, code)

fun getWithdrawalResponsibleText(
    responsible: String,
    documentType: String,
    document: String
): String =
    WITHDRAWAL_RESPONSIBLE_TEXT
        .replace(RESPONSIBLE, responsible)
        .replace(RESPONSIBLE_DOCUMENT_TYPE, documentType)
        .replace(RESPONSIBLE_DOCUMENT, document)

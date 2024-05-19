package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.commons.CODE
import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.commons.TURN_ID
import com.skgtecnologia.sisem.commons.chipOptionsResponseMock
import com.skgtecnologia.sisem.commons.chipSelectionResponseMock
import com.skgtecnologia.sisem.commons.footerResponseMock
import com.skgtecnologia.sisem.commons.headerResponseMock
import com.skgtecnologia.sisem.commons.humanBodyResponseMock
import com.skgtecnologia.sisem.commons.infoCardResponseMock
import com.skgtecnologia.sisem.commons.noveltyMock
import com.skgtecnologia.sisem.commons.simpleCardResponseMock
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.di.operation.OperationRole
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PreOperationalRemoteDataSourceTest {

    @MockK
    private lateinit var preOperationalApi: PreOperationalApi

    private lateinit var preOperationalRemoteDataSource: PreOperationalRemoteDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        preOperationalRemoteDataSource = PreOperationalRemoteDataSource(preOperationalApi)
    }

    @Test
    fun `when getPreOperationalScreen with AUXILIARY_AND_OR_TAPH is success`() = runTest {
        val screenResponse = ScreenResponse(
            header = headerResponseMock,
            body = listOf(humanBodyResponseMock),
            footer = footerResponseMock
        )
        coEvery { preOperationalApi.getAuxPreOperationalScreen(any()) } returns Response.success(
            screenResponse
        )

        val result = preOperationalRemoteDataSource.getPreOperationalScreen(
            role = OperationRole.AUXILIARY_AND_OR_TAPH,
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(screenResponse.mapToDomain(), result.getOrNull())
    }

    @Test
    fun `when getPreOperationalScreen with DRIVER is success`() = runTest {
        val screenResponse = ScreenResponse(
            header = headerResponseMock,
            body = listOf(humanBodyResponseMock),
            footer = footerResponseMock
        )
        coEvery { preOperationalApi.getDriverPreOperationalScreen(any()) } returns Response.success(
            screenResponse
        )

        val result = preOperationalRemoteDataSource.getPreOperationalScreen(
            role = OperationRole.DRIVER,
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(screenResponse.mapToDomain(), result.getOrNull())
    }

    @Test
    fun `when getPreOperationalScreen with MEDIC_APH is success`() = runTest {
        val screenResponse = ScreenResponse(
            header = headerResponseMock,
            body = listOf(humanBodyResponseMock),
            footer = footerResponseMock
        )
        coEvery { preOperationalApi.getDoctorPreOperationalScreen(any()) } returns Response.success(
            screenResponse
        )

        val result = preOperationalRemoteDataSource.getPreOperationalScreen(
            role = OperationRole.MEDIC_APH,
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(screenResponse.mapToDomain(), result.getOrNull())
    }

    @Test
    fun `when getPreOperationalScreen with LEAD_APH failure`() = runTest {
        val result = preOperationalRemoteDataSource.getPreOperationalScreen(
            role = OperationRole.LEAD_APH,
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when getPreOperationalScreen is failure`() = runTest {
        coEvery { preOperationalApi.getAuxPreOperationalScreen(any()) } returns Response.error(
            400,
            "".toResponseBody()
        )

        val result = preOperationalRemoteDataSource.getPreOperationalScreen(
            role = OperationRole.AUXILIARY_AND_OR_TAPH,
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when sendPreOperational is success`() = runTest {
        coEvery { preOperationalApi.sendAuxPreOperational(any()) } returns Response.success(Unit)

        val result = preOperationalRemoteDataSource.sendPreOperational(
            role = OperationRole.AUXILIARY_AND_OR_TAPH,
            idTurn = TURN_ID,
            findings = mapOf(),
            inventoryValues = mapOf(),
            fieldsValues = mapOf(),
            novelties = listOf(noveltyMock)
        )

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when sendPreOperational is failure`() = runTest {
        coEvery { preOperationalApi.sendDriverPreOperational(any()) } returns Response.error(
            400,
            "".toResponseBody()
        )

        val result = preOperationalRemoteDataSource.sendPreOperational(
            role = OperationRole.AUXILIARY_AND_OR_TAPH,
            idTurn = TURN_ID,
            findings = mapOf(),
            inventoryValues = mapOf(),
            fieldsValues = mapOf(),
            novelties = listOf(noveltyMock)
        )

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when sendFindings is success`() = runTest {
        coEvery {
            preOperationalApi.sendAuxFinding(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.success(Unit)

        val result = preOperationalRemoteDataSource.sendFindings(
            role = OperationRole.AUXILIARY_AND_OR_TAPH,
            idTurn = TURN_ID,
            novelties = listOf(noveltyMock)
        )
    }

    @Test
    fun `when getAuthCardViewScreen is success`() = runTest {
        val screenResponse = ScreenResponse(
            header = headerResponseMock,
            body = listOf(infoCardResponseMock),
            footer = footerResponseMock
        )
        coEvery { preOperationalApi.getAuthCardViewScreen(any()) } returns Response.success(
            screenResponse
        )

        val result = preOperationalRemoteDataSource.getAuthCardViewScreen(
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(screenResponse.mapToDomain(), result.getOrNull())
    }

    @Test
    fun `when getAuthCardViewScreen is failure`() = runTest {
        coEvery { preOperationalApi.getAuthCardViewScreen(any()) } returns Response.error(
            400,
            "".toResponseBody()
        )

        val result = preOperationalRemoteDataSource.getAuthCardViewScreen(
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when getPreOperationalScreenView with AUXILIARY_AND_OR_TAPH is success`() = runTest {
        val screenResponse = ScreenResponse(
            header = headerResponseMock,
            body = listOf(simpleCardResponseMock),
            footer = footerResponseMock
        )
        coEvery {
            preOperationalApi.getAuxPreOperationalScreenView(any())
        } returns Response.success(screenResponse)

        val result = preOperationalRemoteDataSource.getPreOperationalScreenView(
            role = OperationRole.AUXILIARY_AND_OR_TAPH,
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(screenResponse.mapToDomain(), result.getOrNull())
    }

    @Test
    fun `when getPreOperationalScreenView with DRIVER is success`() = runTest {
        val screenResponse = ScreenResponse(
            header = headerResponseMock,
            body = listOf(chipOptionsResponseMock),
            footer = footerResponseMock
        )
        coEvery {
            preOperationalApi.getDriverPreOperationalScreenView(any())
        } returns Response.success(screenResponse)

        val result = preOperationalRemoteDataSource.getPreOperationalScreenView(
            role = OperationRole.DRIVER,
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(screenResponse.mapToDomain(), result.getOrNull())
    }

    @Test
    fun `when getPreOperationalScreenView with MEDIC_APH is success`() = runTest {
        val screenResponse = ScreenResponse(
            header = headerResponseMock,
            body = listOf(chipSelectionResponseMock),
            footer = footerResponseMock
        )
        coEvery {
            preOperationalApi.getDoctorPreOperationalScreenView(any())
        } returns Response.success(screenResponse)

        val result = preOperationalRemoteDataSource.getPreOperationalScreenView(
            role = OperationRole.MEDIC_APH,
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(screenResponse.mapToDomain(), result.getOrNull())
    }

    @Test
    fun `when getPreOperationalScreenView with LEAD_APH failure`() = runTest {
        val result = preOperationalRemoteDataSource.getPreOperationalScreenView(
            role = OperationRole.LEAD_APH,
            androidId = SERIAL,
            vehicleCode = CODE,
            idTurn = TURN_ID
        )

        Assert.assertEquals(true, result.isFailure)
    }
}

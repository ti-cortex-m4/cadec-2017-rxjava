package controllers;

import io.reactivex.Single;
import model.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static utils.FutureUtils.fromSingle;

@Singleton
public class BookStoreController extends Controller {
	private final HttpExecutionContext exec;
	private final Form<OrderForm> orderForm;
	private final DroneOrderService droneOrderService;
	private final BookOrderService bookOrderService;

	@Inject
	public BookStoreController(DroneOrderService droneOrderService, BookOrderService bookOrderService,
	                           HttpExecutionContext exec, FormFactory formFactory) {
		this.exec = exec;
		this.orderForm = formFactory.form(OrderForm.class);
		this.droneOrderService = droneOrderService;
		this.bookOrderService = bookOrderService;
	}
	public Result getOrderForm() {
		return ok(views.html.order.render(orderForm));
	}



	public CompletionStage<Result> showDelivery(int orderId) {
		final Single<Order> order = bookOrderService.getOrder(orderId);

		return fromSingle(order).thenApplyAsync(o -> ok(views.html.delivery.render(o)), exec.current());
	}


	public CompletionStage<Result> placeOrder() {
		final Form<OrderForm> boundForm = orderForm.bindFromRequest();

		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.order.render(boundForm)));
		}

		return fromSingle(droneOrderService.placeDroneOrder(boundForm.get()))
				.thenApplyAsync(order -> ok(views.html.orderPlaced.render(order)), exec.current());
	}

}

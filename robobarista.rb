class RoboBarista
	def initialize
		@ingredients = {
			coffee: 				{ units: 10, cost: 0.75 },
			decaf_coffee: 	{ units: 10, cost: 0.75 },
			sugar: 					{ units: 10, cost: 0.25 },
			cream: 					{ units: 10, cost: 0.25 },
			steamed_milk: 	{ units: 10, cost: 0.35 },
			foamed_milk: 		{ units: 10, cost: 0.35 },
			espresso: 			{ units: 10, cost: 0.75 },
			cocoa: 					{ units: 10, cost: 0.90 },
			whipped_cream: 	{ units: 10, cost: 1.00 }
		}.sort.to_h

		@menu = {
			coffee: 					{ coffee: 3, sugar: 1, cream: 1 },
			decaf_coffee: 		{ decaf_coffee: 3, sugar: 1, cream: 1 },
			caffe_latte: 			{ espresso: 2, steamed_milk: 1 },
			caffe_americano: 	{ espresso: 3 },
			caffe_mocha: 			{ espresso: 1, cocoa: 1, steamed_milk: 1, whipped_cream: 1 },
			cappuccino: 			{ espresso: 2, steamed_milk: 1, foamed_milk: 1 }
		}.sort.to_h

		@valid_inputs = ['r', 'q', ('1'..@menu.length.to_s).to_a].flatten
		
		print_inventory
		print_menu
		get_input
	end

	def get_input
		@user_input = gets.chomp.downcase
		case
		when !@valid_inputs.include?(@user_input)
			puts "Invalid selection: #{@user_input}"
			print_inventory
			print_menu
			get_input
		when @user_input == 'r'
			restock
			print_menu
			get_input
		when @user_input == 'q'
			return
		when ('1'..@menu.length.to_s).include?(@user_input)
			drink_sym = @menu.to_a[@user_input.to_i - 1].first
			if is_available?(drink_sym)
				dispense(drink_sym)
				print_inventory
				print_menu
				get_input
			elsif !is_available?(drink_sym)
				puts "Out of stock: #{pretty_print(drink_sym)}"
				print_inventory
				print_menu
				get_input
			end
		end
	end

	def dispense(drink)
		drink_ingredients = @menu[drink.to_sym]
		drink_ingredients.each do |ingredient, units|
			decrement_ingredient_units(ingredient, units)
		end
		puts "Dispensing: #{pretty_print(drink)}"
	end

	def restock
		@ingredients.each do |ingredient, vals|
			vals[:units] = 10
		end
	end

	def print_inventory
		puts "Inventory:"
		@ingredients.each do |ingredient, val|
			puts "#{ingredient},#{val[:units]}"
		end
	end

	def print_menu
		puts "Menu:"
		i = 1
		@menu.each_key do |drink|
			puts "#{i},#{pretty_print(drink)},$#{drink_cost(drink)},#{is_available?(drink)}"
			i += 1
		end
	end

	def pretty_print(drink)
		drink_str = drink.to_s.gsub('_', ' ')
		pretty_printed_drink = ""
		drink_str.split(' ').each do |word|
			pretty_printed_drink << "#{word.capitalize} "
		end
		pretty_printed_drink.chomp(' ')
	end

	def drink_cost(drink)
		ingredients = @menu[drink.to_sym]
		cost = 0
		ingredients.each do |ingredient, units|
			cost += @ingredients[ingredient.to_sym][:cost] * units
		end
		cost
	end

	def is_available?(drink)
		ingredients = @menu[drink.to_sym]
		ingredients.each do |ingredient, units|
			if @ingredients[ingredient.to_sym][:units] < units
				return false
			end
		end
		true
	end

	def decrement_ingredient_units(ingredient, units_consumed)
		@ingredients[ingredient.to_sym][:units] -= units_consumed
	end
end